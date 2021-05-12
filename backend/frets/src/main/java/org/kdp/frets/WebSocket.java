package org.kdp.frets;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws")
@ApplicationScoped
public class WebSocket
{
    private final Map<String, Session> session = new ConcurrentHashMap<>();

    @Inject
    Logger log;

    @Inject
    DatabaseConnection conn;

    @OnOpen
    public void onOpen(Session session)
    {
        log.info("new session: " + session.getId());
        log.info(conn.getJdbi());
    }
}
