package org.kdp.frets.game;

import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.user.UserDao;
import org.kdp.frets.websocket.WebSocket;
import org.kdp.frets.websocket.response.Response;
import org.kdp.frets.websocket.response.responses.GamesResponse;
import org.kdp.frets.websocket.response.responses.JoinGameResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class GameController
{
    @Inject
    Logger log;

    @Inject
    WebSocket webSocket;

    @Inject
    ManagedExecutor executor;

    @Inject
    UserDao userDao;

    @Inject
    GameDao gameDao;

    public void broadcastGames()
    {
        executor.submit(() -> {
            log.info("broadcasting games...");
            final var games = gameDao.getAllByNewest();
            webSocket.broadcast(new GamesResponse(games));
        });
    }

    public void sendGamesToSessionId(String sessionId)
    {
        executor.submit(() -> {
            log.info("sending games to session: " + sessionId);
            final var games = gameDao.getAllByNewest();
            webSocket.sendToSessionId(sessionId, new GamesResponse(games));
        });
    }

    public void notifyPlayers(Long gameId, Response response)
    {
        executor.submit(() -> {
            final var game = gameDao.getById(gameId).orElseThrow();
            final var sessionIds = gameDao.getSessionIds(game);
            webSocket.sendToSessionIds(sessionIds, response);
        });
    }

    public void createGame(String sessionId)
    {
        executor.submit(() -> {
            final var user = userDao.getBySessionId(sessionId).orElseThrow();
            final var game = new Game();
            game.addPlayerId(user.id);
            game.setHostId(user.id);
            log.info("creating game: " + game);
            gameDao.create(game);
            broadcastGames();
            webSocket.sendToSessionId(sessionId, new JoinGameResponse(game));
        });
    }

    public void addUserToGame(Long gameId, Long userId)
    {
        executor.submit(() -> {
            final var game = gameDao.getById(gameId).orElseThrow();
            game.addPlayerId(userId);
            gameDao.updatePlayerIds(game);
            broadcastGames();
            notifyPlayers(game.id, new JoinGameResponse(game));
        });
    }

    @Scheduled(every = "30s")
    public void broadcastGameUpdates()
    {
        broadcastGames();
    }

    @Scheduled(every = "1m")
    public void cleanupFinishedGames()
    {
        executor.submit(() -> {
            gameDao.getAll().forEach(game -> {
                final var isOver = game.getState() == Game.State.GAME_OVER;
                final var isOld = game.createdAt.isBefore(
                        Instant.now().minus(1, ChronoUnit.MINUTES));

                if (isOver && isOld) {
                    log.info("deleting old game: " + game);
                    gameDao.delete(game);
                    broadcastGames();
                }
            });
        });
    }
}
