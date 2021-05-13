package org.kdp.frets.user;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.websocket.WebSocket;
import org.kdp.frets.websocket.message.LoginMessage;
import org.kdp.frets.websocket.response.LoginResponse;

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

    public void loginAnonymousUser(String sessionId)
    {
        executor.supplyAsync(() -> {
            final var user = new User(sessionId);
            userDao.create(user);
            return user;
        }).thenAcceptAsync(user -> {
            webSocket.sendToSessionId(sessionId, new LoginResponse(user));
        });
    }

    public void login(String sessionId, LoginMessage message)
    {
        executor.supplyAsync(() -> {
            final var name = message.name;
            final var user = userDao
                    .getBySessionId(sessionId)
                    .orElseThrow()
                    .withName(message.name);
            userDao.updateName(user.id(), name);
            return user;
        }).thenAcceptAsync(user -> {
            log.info("hmm " + user);
            webSocket.sendToSessionId(sessionId, new LoginResponse(user));
        });
    }

//    public void logout(String sessionId)
//    {
//        executor.supplyAsync(() -> userDao
//                .getBySessionId(sessionId)
//                .orElseThrow()
//                .withName(User.DEFAULT_NAME))
//                .thenAcceptAsync(user -> {
//                    webSocket.sendToSession(sessionId, new LoginResponse(user));
//                });
//    }

    public void sessionClosed(String sessionId)
    {
        executor.submit(() -> userDao.deleteBySessionId(sessionId));
    }
}
