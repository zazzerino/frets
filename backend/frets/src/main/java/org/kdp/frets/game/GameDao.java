package org.kdp.frets.game;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameDao
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    public Optional<Game> getById(Long gameId)
    {
        return dbConn.getJdbi().withHandle(handle -> handle
                .select("SELECT * FROM games WHERE id = ?", gameId)
                .mapTo(Game.class)
                .findFirst());
    }

//    public Optional<Game> getByUserSessionId(String sessionId)
//    {
//        return Optional.empty();
//    }

//    select users.session_id from games join users on games.id = users.game_id where users.id = 2;
//    select users.id, name, session_id, game_id from users join games on games.id = users.game_id where users.id = 0;

    public List<Game> getAll()
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM games")
                        .mapTo(Game.class)
                        .list());
    }

    public List<Game> getAllByNewest()
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM games ORDER BY created_at DESC")
                        .mapTo(Game.class)
                        .list());
    }

    public void create(Game game)
    {
        try {
            dbConn.getJdbi().useHandle(handle -> {
                final var update = handle.createUpdate("""
                        INSERT INTO games (id, created_at, host_id, state, player_ids)
                        VALUES (:id, :created_at, :host_id, :state, :player_ids)""")
                        .bind("id", game.id)
                        .bind("created_at", game.createdAt)
                        .bind("host_id", game.hostId)
                        .bind("state", game.getState())
                        .bindArray("player_ids", Long.class, game.getPlayerIds().toArray())
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
        try {
            dbConn.getJdbi().useHandle(handle -> {
                handle.createUpdate("""
                        UPDATE games
                        SET player_ids = :player_ids, state = :state
                        WHERE id = :id""")
                        .bind("id", game.id)
                        .bind("state", game.getState())
                        .bindArray("player_ids", Long.class, game.getPlayerIds().toArray())
                        .execute();
            });
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
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
