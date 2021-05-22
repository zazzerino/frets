package org.kdp.frets.game;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameDao
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    @Inject
    UserDao userDao;

    public Optional<Game> getById(Long gameId)
    {
        try (final var handle = dbConn.getJdbi().open()) {
            final var game = handle
                    .select("SELECT * FROM games WHERE id = ?", gameId)
                    .mapTo(Game.class)
                    .findOne();

            final var users = handle
                    .select("SELECT * FROM users WHERE game_id = ?", gameId)
                    .mapTo(User.class)
                    .collect(Collectors.toSet());

            game.ifPresent(g -> g.setUsers(users));

            return game;
        }
    }

    public List<Game> getAll()
    {
        try (final var handle = dbConn.getJdbi().open()) {
            final var games = handle
                    .select("SELECT * FROM games")
                    .mapTo(Game.class)
                    .list();

            games.forEach(game -> {
                final var users = handle
                        .select("SELECT * FROM users WHERE game_id = ?", game.id)
                        .mapTo(User.class)
                        .collect(Collectors.toSet());

                game.setUsers(users);
            });

            return games;
        }
    }

    public List<Game> getAllByNewest()
    {
        try (final var handle = dbConn.getJdbi().open()) {
            final var games = handle
                    .select("SELECT * FROM games ORDER BY created_at DESC")
                    .mapTo(Game.class)
                    .list();

            games.forEach(game -> {
                final var users = handle
                        .select("SELECT * FROM users WHERE game_id = ?", game.id)
                        .mapTo(User.class)
                        .collect(Collectors.toSet());

                game.setUsers(users);
            });

            return games;
        }
    }

    public void create(Game game)
    {
        try (final var handle = dbConn.getJdbi().open()) {
            handle.execute("""
                        INSERT INTO games (id, created_at, state, host_id)
                        VALUES (?, ?, ?, ?)""",
                    game.id,
                    game.createdAt,
                    game.getState(),
                    game.getHostId());

            for (final var user : game.getUsers()) {
                user.setGameId(game.id);
                userDao.update(user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void update(Game game) {
        dbConn.getJdbi().useHandle(handle -> handle
                .execute("""
                        UPDATE games
                        SET created_at = ?,  state = ?, host_id = ?
                        WHERE id = ?""",
                        game.createdAt,
                        game.getState(),
                        game.getHostId(),
                        game.id));
    }

    public void delete(Game game)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> handle
                    .execute("DELETE FROM games WHERE id = ?", game.id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<String> getSessionIds(Game game)
    {
        try {
            return dbConn.getJdbi()
                    .withHandle(handle -> handle
                            .select("SELECT session_id FROM users WHERE game_id = :id")
                            .bind("id", game.id)
                            .mapTo(String.class)
                            .list());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

//    public List<User> getPlayers(Game game)
//    {
//        return dbConn.getJdbi()
//                .withHandle(handle -> handle
//                        .select("SELECT * FROM users WHERE id in (<player_ids>)")
//                        .bindList("player_ids", game.getPlayerIds())
//                        .mapTo(User.class)
//                        .list());
//    }

//    public List<Game> getUserGames(Long userId)
//    {
//        return dbConn.getJdbi()
//                .withHandle(handle -> handle
//                        .select("SELECT * FROM games WHERE :user_id = ANY(player_ids)")
//                        .bind("user_id", userId)
//                        .mapTo(Game.class)
//                        .list());
//    }
}
