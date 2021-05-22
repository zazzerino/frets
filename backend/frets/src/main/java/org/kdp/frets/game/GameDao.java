package org.kdp.frets.game;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;
import org.kdp.frets.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

//    select users.session_id from games join users on games.id = users.game_id where users.id = 2;
//    select users.id, name, session_id, game_id from users join games on games.id = users.game_id where users.id = 0;

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

//        return dbConn.getJdbi()
//                .withHandle(handle -> handle
//                        .select("SELECT * FROM games")
//                        .mapTo(Game.class)
//                        .list());
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

//        return dbConn.getJdbi()
//                .withHandle(handle -> handle
//                        .select("SELECT * FROM games ORDER BY created_at DESC")
//                        .mapTo(Game.class)
//                        .list());
    }

    public void create(Game game)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> {
                final var update = handle.createUpdate("""
                        INSERT INTO games (id, created_at, state, host_id)
                        VALUES (:id, :created_at, :state, :host_id)""")
                        .bind("id", game.id)
                        .bind("created_at", game.createdAt)
                        .bind("state", game.getState())
                        .bind("host_id", game.hostId)
                        .execute();
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
    }

    public void delete(Game game)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.execute("DELETE FROM games WHERE id = ?", game.id);
        });
    }

    public void updatePlayerIdsAndState(Game game)
    {
//        try {
//            dbConn.getJdbi().useHandle(handle -> {
//                handle.createUpdate("""
//                        UPDATE games
//                        SET player_ids = :player_ids, state = :state
//                        WHERE id = :id""")
//                        .bind("id", game.id)
//                        .bind("state", game.getState())
//                        .bindArray("player_ids", User.class, game.getUsers().toArray())
//                        .execute();
//            });
//        } catch (Exception e) {
//            log.error(e.getStackTrace());
//        }
    }

    public List<String> getSessionIds(Game game)
    {
//        return dbConn.getJdbi()
//                .withHandle(handle -> handle
//                        .select("SELECT session_id FROM users WHERE id in (<player_ids>)")
//                        .bindList("player_ids", game.getPlayerIds())
//                        .mapTo(String.class)
//                        .list());
        return List.of();
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

    public List<Game> getUserGames(Long userId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM games WHERE :user_id = ANY(player_ids)")
                        .bind("user_id", userId)
                        .mapTo(Game.class)
                        .list());
    }
}
