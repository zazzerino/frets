package org.kdp.frets.user;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
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

    public void loginAnonymousUser(String sessionId)
    {
        try {
            executor.submit(() -> {
                final var user = new User(sessionId);
                userDao.create(user);
                webSocket.sendToSessionId(sessionId, new LoginResponse(user));
                log.info("saving user: " + user);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void login(String sessionId, LoginMessage message)
    {
        try {
            executor.submit(() -> {
                final var user = userDao.getBySessionId(sessionId).orElseThrow();
                user.setName(message.name);
                userDao.updateName(user);
                webSocket.sendToSessionId(sessionId, new LoginResponse(user));
                log.info("user logged in: " + user);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void sessionClosed(User user)
    {
        try {
            executor.submit(() -> {
                userDao.delete(user);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }
}
