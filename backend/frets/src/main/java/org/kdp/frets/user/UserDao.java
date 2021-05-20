package org.kdp.frets.user;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao
{
    @Inject
    Logger log;

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
        try {
            return dbConn.getJdbi()
                    .withHandle(handle -> handle
                            .select("SELECT * FROM users WHERE id = ?", userId)
                            .mapTo(User.class)
                            .findFirst());
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return Optional.empty();
    }

    public Optional<User> getBySessionId(String sessionId)
    {
        try {
            return dbConn.getJdbi()
                    .withHandle(handle -> handle
                            .select("SELECT * FROM users WHERE session_id = ?", sessionId)
                            .mapTo(User.class)
                            .findFirst());
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return Optional.empty();
    }

    public void create(User user)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> handle
                    .execute("""
                                    INSERT INTO users (id, name, session_id, game_id)
                                    VALUES (?, ?, ?, ?)""",
                            user.id,
                            user.getName(),
                            user.sessionId,
                            user.getGameId())
            );
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void delete(User user)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> {
                handle.execute("DELETE FROM users WHERE id = ?", user.id);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void updateName(User user)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> {
                handle.execute("UPDATE users SET name = ? WHERE id = ?",
                        user.getName(),
                        user.id);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void updateGameId(User user)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> {
                handle.execute("UPDATE users SET game_id = ? WHERE id = ?",
                        user.getGameId(),
                        user.id);
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    //    public void removeUserFromCurrentGame(Long userId)
//    {
//        try {
//            executor.submit(() -> {
//                final var user = userDao.getById(userId).orElseThrow();
//
//                if (user.getGameId() != null) {
//                    final var game = gameDao.getById(user.getGameId()).orElseThrow();
//
//                    game.removePlayerId(user.id);
//                    gameDao.updatePlayerIdsAndState(game);
//
//                    user.setGameId(null);
//                    userDao.updateGameId(user);
//                }
//            });
//        } catch (Exception e) {
//            log.error(e.getStackTrace());
//        }
//    }
}
