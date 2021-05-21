package org.kdp.frets.game;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.user.User;
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

    /**
     * Sends info about each game to every connected client.
     */
    public void broadcastGames()
    {
        executor.submit(() -> {
            log.info("broadcasting games...");
            final var games = gameDao.getAllByNewest();
            webSocket.broadcast(new GamesResponse(games));
        });
    }

    /**
     * Sends info about each game to the given session.
     */
    public void sendGamesToSessionId(String sessionId)
    {
        executor.submit(() -> {
            log.info("sending games to session: " + sessionId);
            final var games = gameDao.getAllByNewest();
            webSocket.sendToSessionId(sessionId, new GamesResponse(games));
        });
    }

    /**
     * Send a response to each player of the specified game.
     */
    public void notifyPlayers(Long gameId, Response response)
    {
        executor.submit(() -> {
            final var game = gameDao.getById(gameId).orElseThrow();
            final var sessionIds = gameDao.getSessionIds(game);
            webSocket.sendToSessionIds(sessionIds, response);
        });
    }

    /**
     * Creates a new game, adds the user to it as host, and then broadcasts the new game info to each client.
     */
    public void createGame(String sessionId)
    {
        executor.submit(() -> {
            final var user = userDao.getBySessionId(sessionId).orElseThrow();
            removeUserFromCurrentGame(user);

            final var game = new Game(user.id);
            log.info("creating game: " + game);
            gameDao.create(game);

            user.setGameId(game.id);
            userDao.updateGameId(user);

            webSocket.sendToSessionId(sessionId, new JoinGameResponse(game));
            broadcastGames();
        });
    }

    public void removeUserFromCurrentGame(User user)
    {
        executor.submit(() -> {
            if (user.getGameId() != null) {
                final var game = gameDao.getById(user.getGameId()).orElseThrow();

                game.removePlayerId(user.id);
                gameDao.updatePlayerIdsAndState(game);

                notifyPlayers(game.id, new GameUpdatedResponse(game));
                broadcastGames();
            }
        });
    }

    public void addUserToGame(Long gameId, Long userId)
    {
        executor.submit(() -> {
            final var user = userDao.getById(userId).orElseThrow();
            removeUserFromCurrentGame(user);

            final var game = gameDao.getById(gameId).orElseThrow();
            game.addPlayerId(userId);
            gameDao.updatePlayerIdsAndState(game);

            user.setGameId(game.id);
            userDao.updateGameId(user);

            notifyPlayers(gameId, new JoinGameResponse(game));
            broadcastGames();
        });
    }

//    @Scheduled(every = "5m")
//    public void cleanupFinishedGames()
//    {
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
//    }
}
