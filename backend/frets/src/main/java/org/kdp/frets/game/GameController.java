package org.kdp.frets.game;

import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.user.UserDao;
import org.kdp.frets.websocket.WebSocket;
import org.kdp.frets.websocket.response.Response;
import org.kdp.frets.websocket.response.responses.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
        try {
            executor.submit(() -> {
                log.info("broadcasting games...");
                final var games = gameDao.getAllByNewest();
                log.info("games: " + games);
                webSocket.broadcast(new GamesResponse(games));
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void sendGamesToSessionId(String sessionId)
    {
        try {
            executor.submit(() -> {
                log.info("sending games to session: " + sessionId);
                final var games = gameDao.getAllByNewest();
                webSocket.sendToSessionId(sessionId, new GamesResponse(games));
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void notifyPlayers(Long gameId, Response response)
    {
//        executor.submit(() -> {
//            final var game = gameDao.getById(gameId).orElseThrow();
//            final var sessionIds = gameDao.getSessionIds(game);
//            webSocket.sendToSessionIds(sessionIds, response);
//        });
    }

    public void createGame(String sessionId)
    {
        try {
            executor.submit(() -> {
                final var user = userDao.getBySessionId(sessionId).orElseThrow();
                final var game = new Game(user.id);
                log.info("creating game: " + game);

                user.setGameId(game.id);
                userDao.updateGameId(user);

                gameDao.create(game);
                webSocket.sendToSessionId(sessionId, new JoinGameResponse(game));
                broadcastGames();
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void addUserToGame(Long gameId, Long userId)
    {
//        executor.submit(() -> {
//            final var game = gameDao.getById(gameId).orElseThrow();
//            game.addPlayerId(userId);
//            gameDao.updatePlayerIds(game);
//            broadcastGames();
//            notifyPlayers(game.id, new JoinGameResponse(game));
//        });
    }

//    @Scheduled(every = "10s")
//    public void broadcastGameUpdates()
//    {
//        broadcastGames();
//    }

    @Scheduled(every = "5m")
    public void cleanupFinishedGames()
    {
//        executor.submit(() -> {
//            gameDao.getAll().forEach(game -> {
//                final var isOver = game.getState() == Game.State.GAME_OVER;
//                final var isOld = game.createdAt.isBefore(
//                        Instant.now().minus(5, ChronoUnit.MINUTES));
//
//                if (isOver && isOld) {
//                    log.info("deleting game: " + game);
//                    gameDao.delete(game);
//                    broadcastGames();
//                }
//            });
//        });
    }
}
