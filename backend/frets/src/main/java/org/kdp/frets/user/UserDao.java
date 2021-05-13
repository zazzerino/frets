package org.kdp.frets.user;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserDao
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    public Optional<User> getById(Long userId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM users WHERE id = ?", userId)
                        .mapTo(User.class)
                        .stream()
                        .findFirst());
    }

    public Optional<User> getBySessionId(String sessionId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM users WHERE session_id = ?", sessionId)
                        .mapTo(User.class)
                        .stream().findFirst());
    }

    public void save(User user)
    {
        log.info("saving user: " + user);

        dbConn.getJdbi().useHandle(handle -> {
            handle.execute(
                    "INSERT INTO users (id, name, session_id, game_id) VALUES (?, ?, ?, ?)",
                    user.id(), user.name(), user.sessionId(), user.gameId());
        });
    }

    public void deleteById(Long id)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.execute("DELETE FROM users WHERE id = ?", id);
        });
    }
}
