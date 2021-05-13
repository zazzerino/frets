package org.kdp.frets.user;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import org.kdp.frets.websocket.WebSocket;
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
            userDao.save(user);
            return user;
        }).thenAcceptAsync(user -> {
            webSocket.sendToSession(sessionId, new LoginResponse(user));
        });
    }

    public void logout(String sessionId)
    {
    }
}
