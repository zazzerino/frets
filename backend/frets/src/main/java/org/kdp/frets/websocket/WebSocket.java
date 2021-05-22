package org.kdp.frets.websocket;

import org.jboss.logging.Logger;
import org.kdp.frets.game.GameController;
import org.kdp.frets.user.UserController;
import org.kdp.frets.user.UserDao;
import org.kdp.frets.websocket.message.*;
import org.kdp.frets.websocket.message.messages.*;
import org.kdp.frets.websocket.response.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/ws",
        decoders = { MessageDecoder.class },
        encoders = { ResponseEncoder.class }
)
@ApplicationScoped
public class WebSocket
{
    @Inject
    Logger log;

    @Inject
    UserController userController;

    @Inject
    GameController gameController;

    @Inject
    UserDao userDao;

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session)
    {
        final var sessionId = session.getId();
        log.info("new session: " + sessionId);

        sessions.put(sessionId, session);
        userController.loginAnonymousUser(sessionId);
        gameController.sendGamesToSessionId(sessionId);
    }

    @OnClose
    public void onClose(Session session)
    {
        final var sessionId = session.getId();
        log.info("removing session: " + sessionId);
        sessions.remove(sessionId);

        final var user = userDao.getBySessionId(sessionId).orElseThrow();
        userController.sessionClosed(user);
        gameController.removeFromCurrentGame(user);
    }

    @OnMessage
    public void onMessage(Session session, Message message)
    {
        log.info("message received: " + message);

        if (message instanceof LoginMessage l) {
            userController.login(session.getId(), l);
        } else if (message instanceof CreateGameMessage) {
            gameController.createGame(session.getId());
        } else if (message instanceof JoinGameMessage j) {
            gameController.addUserToGame(j.gameId, j.userId);
        } else {
            log.error("unrecognized message type: " + message);
        }
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

    public void sendToSessionId(String sessionId, Response response)
    {
        sendToSession(sessions.get(sessionId), response);
    }

    public void sendToSessionIds(Collection<String> sessionIds, Response response)
    {
        sessionIds.forEach(id -> sendToSessionId(id, response));
    }

    public void broadcast(Response response)
    {
        sessions.values()
                .forEach(s -> sendToSession(s, response));
    }
}
