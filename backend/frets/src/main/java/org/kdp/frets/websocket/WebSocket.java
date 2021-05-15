package org.kdp.frets.websocket;

import org.jboss.logging.Logger;
import org.kdp.frets.game.GameController;
import org.kdp.frets.user.UserController;
import org.kdp.frets.websocket.message.*;
import org.kdp.frets.websocket.message.messages.CreateGameMessage;
import org.kdp.frets.websocket.message.messages.LoginMessage;
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
        decoders = {MessageDecoder.class},
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

    @Inject
    GameController gameController;

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
        userController.sessionClosed(session.getId());
    }

    @OnMessage
    public void onMessage(Session session, Message message)
    {
        log.info("message received: " + message);

        if (message instanceof LoginMessage l) {
            userController.login(session.getId(), l);
        } else if (message instanceof CreateGameMessage) {
            gameController.createGame(session.getId());
        } else {
            log.error("unrecognized message type: " + message);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        log.error("WEBSOCKET ERROR", throwable);
    }

    public void sendToSessionId(Session session, Response response)
    {
        session.getAsyncRemote()
                .sendObject(response, result -> {
                    if (result.getException() != null) {
                        log.error("error sending to " + session.getId() + ": " + result.getException());
                    }
                });
    }

    public void sendToSessionId(String sessionId, Response response)
    {
        final var session = sessions.get(sessionId);
        sendToSessionId(session, response);
    }

    public void sendToSessionIds(Collection<String> sessionIds, Response response)
    {
        sessionIds.forEach(id -> sendToSessionId(id, response));
    }

    public void broadcast(Response response)
    {
        sessions.values()
                .forEach(s -> sendToSessionId(s, response));
    }
}
