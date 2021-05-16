package org.kdp.frets.user;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.game.GameController;
import org.kdp.frets.game.GameDao;
import org.kdp.frets.websocket.WebSocket;
import org.kdp.frets.websocket.message.messages.LoginMessage;
import org.kdp.frets.websocket.response.responses.LoginResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserController
{
    @Inject
    Logger log;

    @Inject
    ManagedExecutor executor;

    @Inject
    WebSocket webSocket;

    @Inject
    UserDao userDao;

    @Inject
    GameDao gameDao;

    @Inject
    GameController gameController;

    public void loginAnonymousUser(String sessionId)
    {
        executor.submit(() -> {
            final var user = new User(sessionId);
            log.info("saving user: " + user);
            userDao.create(user);
            webSocket.sendToSessionId(sessionId, new LoginResponse(user));
        });
    }

    public void login(String sessionId, LoginMessage message)
    {
        executor.submit(() -> {
            final var name = message.name;
            final var user = userDao.getBySessionId(sessionId).orElseThrow();
            user.setName(name);
            log.info("user logged in: " + user);
            userDao.updateName(user);
            webSocket.sendToSessionId(sessionId, new LoginResponse(user));
        });
    }

    public void sessionClosed(String sessionId)
    {
        executor.submit(() -> {
            userDao.getBySessionId(sessionId)
                    .ifPresent(user -> {
                        log.info("deleting user: " + user);
                        userDao.deleteBySessionId(sessionId);
                        gameDao.getUserGames(user.id).forEach(game -> {
                            game.removePlayerId(user.id);
                            gameDao.updatePlayerIds(game);
                        });

//                        gameDao.getUserGames(user.id)
//                                .ifPresent(game -> {
//                                    game.removePlayerId(user.id);
//                                    gameDao.updatePlayerIds(game);
//                                    gameController.notifyPlayers(game.id, new GameUpdatedResponse(game));
//                                    gameController.broadcastGames();
//                                });
                    });
        });
    }
}
