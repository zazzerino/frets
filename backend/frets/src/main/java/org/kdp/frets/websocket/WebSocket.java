package org.kdp.frets.websocket;

import org.jboss.logging.Logger;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws")
@ApplicationScoped
public class WebSocket
{
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    Logger log;

    @Inject
    UserDao userDao;

    @OnOpen
    public void onOpen(Session session)
    {
        log.info("new session: " + session.getId());
        sessions.put(session.getId(), session);
        final var user = new User(session.getId());
        log.info("user: " + user);
        userDao.save(user);
    }

    @OnClose
    public void onClose(Session session)
    {
        log.info("removing session: " + session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        log.error("WEBSOCKET ERROR", throwable);
    }
}
