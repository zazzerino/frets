package org.kdp.frets.websocket;

import org.jboss.logging.Logger;
import org.kdp.frets.user.UserController;
import org.kdp.frets.websocket.response.Response;
import org.kdp.frets.websocket.response.ResponseEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/ws",
        encoders = {ResponseEncoder.class}
)
@ApplicationScoped
public class WebSocket
{
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    Logger log;

    @Inject
    UserController userController;

    @OnOpen
    public void onOpen(Session session)
    {
        log.info("new session: " + session.getId());
        sessions.put(session.getId(), session);
        userController.loginAnonymousUser(session.getId());
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

    public void sendToSession(Session session, Response response)
    {
        session.getAsyncRemote()
                .sendObject(response, result -> {
                    if (result.getException() != null) {
                        log.error("error sending to " + session.getId() + ": " + result.getException());
                    }
                });
    }

    public void sendToSession(String sessionId, Response response)
    {
        final var session = sessions.get(sessionId);
        sendToSession(session, response);
    }

    public void sendToSessions(Collection<String> sessionIds, Response response)
    {
        sessionIds.forEach(id -> sendToSession(id, response));
    }

    public void broadcast(Response response)
    {
        sessions.values()
                .forEach(s -> sendToSession(s, response));
    }
}
