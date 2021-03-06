package org.kdp.frets.user;

import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao
{
    @Inject
    DatabaseConnection dbConn;

    public List<User> getAll()
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM users")
                        .mapTo(User.class)
                        .list());
    }

    public Optional<User> getById(Long userId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM users WHERE id = ?", userId)
                        .mapTo(User.class)
                        .findFirst());
    }

    public Optional<User> getBySessionId(String sessionId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM users WHERE session_id = ?", sessionId)
                        .mapTo(User.class)
                        .findFirst());
    }

    public void create(User user)
    {
        dbConn.getJdbi().useHandle(handle -> handle
                .execute("INSERT INTO users (id, name, session_id, game_id) VALUES (?, ?, ?, ?)",
                        user.id,
                        user.getName(),
                        user.sessionId,
                        user.getGameId())
        );
    }

    public void delete(User user)
    {
        dbConn.getJdbi().useHandle(handle -> handle
                .execute("DELETE FROM users WHERE id = ?", user.id));
    }

    public void update(User user)
    {
        dbConn.getJdbi().useHandle(handle -> handle
            .execute("""
                    UPDATE users
                    SET session_id = ?, name = ?, game_id = ?
                    WHERE id = ?""",
                    user.sessionId,
                    user.getName(),
                    user.getGameId(),
                    user.id));
    }
}
