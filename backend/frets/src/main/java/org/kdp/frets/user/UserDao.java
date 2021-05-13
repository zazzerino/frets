package org.kdp.frets.user;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserDao
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    @Transactional
    public void save(User user)
    {
        log.info("saving user: " + user);

        dbConn.getJdbi().useHandle(handle -> {
            handle.execute("INSERT INTO users (id, name, session_id, game_id) VALUES (?, ?, ?, ?)",
                    user.id(), user.name(), user.sessionId(), user.gameId());
        });
    }
}
