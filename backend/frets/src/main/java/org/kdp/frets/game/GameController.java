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
            try {
                log.info("broadcasting games...");
                final var games = gameDao.getAllByNewest();
                webSocket.broadcast(new GamesResponse(games));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Sends info about each game to the given session.
     */
    public void sendGamesToSessionId(String sessionId)
    {
        executor.submit(() -> {
            try {
                log.info("sending games to session: " + sessionId);
                final var games = gameDao.getAllByNewest();
                webSocket.sendToSessionId(sessionId, new GamesResponse(games));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Send a response to each player of the specified game.
     */
    public void notifyPlayers(Long gameId, Response response)
    {
        executor.submit(() -> {
            try {
                final var game = gameDao.getById(gameId).orElseThrow();
                final var sessionIds = gameDao.getSessionIds(game);
                webSocket.sendToSessionIds(sessionIds, response);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Creates a new game, adds the user to it as host, and then broadcasts the new game info to each client.
     */
    public void createGame(String sessionId)
    {
        executor.submit(() -> {
            try {
                final var user = userDao.getBySessionId(sessionId).orElseThrow();
                removeFromCurrentGame(user);

                final var game = new Game(user);
                log.info("creating game: " + game);
                gameDao.create(game);

                user.setGameId(game.id);
                userDao.update(user);

                webSocket.sendToSessionId(sessionId, new JoinGameResponse(game));
                broadcastGames();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void removeFromCurrentGame(User user)
    {
        executor.submit(() -> {
            try {
                if (user.getGameId() != null) {
                    final var game = gameDao.getById(user.getGameId()).orElseThrow();

                    game.removeUser(user);
                    gameDao.update(game);

                    user.setGameId(null);
                    userDao.update(user);

                    notifyPlayers(game.id, new GameUpdatedResponse(game));
                    broadcastGames();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void addUserToGame(Long gameId, Long userId)
    {
        executor.submit(() -> {
            try {
                final var user = userDao.getById(userId).orElseThrow();
                removeFromCurrentGame(user);

                final var game = gameDao.getById(gameId).orElseThrow();
                game.addUser(user);
                gameDao.update(game);

                user.setGameId(game.id);
                userDao.update(user);

                notifyPlayers(gameId, new JoinGameResponse(game));
                broadcastGames();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
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
