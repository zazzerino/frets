package org.kdp.frets.game;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;
import org.kdp.frets.websocket.WebSocket;
import org.kdp.frets.websocket.response.responses.GamesResponse;

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
        executor.submit(() -> {
            log.info("broadcasting games...");
            final var games = gameDao.getAll();
            webSocket.broadcast(new GamesResponse(games));
        });
    }

    public void createGame(String sessionId)
    {
        executor.submit(() -> {
            try {
                final var user = userDao.getBySessionId(sessionId).orElseThrow();
                final var game = new Game();
                game.addPlayer(user);
                log.info("creating game: " + game);
                gameDao.create(game);
                broadcastGames();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
