package org.kdp.frets.game;

import org.jboss.logging.Logger;
import org.jdbi.v3.core.mapper.RowMapperFactory;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.kdp.frets.DatabaseConnection;
import org.kdp.frets.theory.Accidental;
import org.kdp.frets.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedHashMap;
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
        try {
            return dbConn.getJdbi().withHandle(handle -> handle
                    .select("SELECT * FROM games WHERE id = ?", gameId)
                    .mapTo(Game.class)
                    .findFirst());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

//        return dbConn.getJdbi().withHandle(handle -> handle
//                .createQuery("""
//                    SELECT games.id g_id, created_at g_created_at, host_id g_host_id, state g_state,
//                    users.id u_id, name u_name, session_id u_session_id, game_id u_game_id
//                    FROM games
//                    JOIN users on games.id = users.game_id
//                    WHERE games.id = :id;""")
//                .bind("id", gameId)
//                .map((rs, ctx) -> {
//                    final var user = new User(
//                            rs.getLong("u_id"),
//                            rs.getString("u_name"),
//                            rs.getString("u_session_id"),
//                            rs.getLong("u_game_id"));
//
//                    return new Game(new User("asdf"));
//                })
//                .findFirst());

//                .registerRowMapper(BeanMapper.factory(Game.class, "g"))
//                .registerRowMapper(BeanMapper.factory(User.class, "u"))
//                .reduceRows(new LinkedHashMap<Long, Game>(), (map, rowView) -> {
//                    final var game = map.computeIfAbsent(
//                            rowView.getColumn("g_id", Long.class),
//                            _id -> rowView.getRow(Game.class));
//                    if (rowView.getColumn("u_id", Long.class) != null) {
//                        game.addPlayer(rowView.getRow(User.class));
//                    }
//                    return map;
//                })
//                .values()
//                .stream()
//                .findFirst());

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
            e.printStackTrace();
        }

//        dbConn.getJdbi().useHandle(handle -> {
//            handle.createUpdate("""
//                        INSERT INTO games
//                        (id, created_at, host_id, state, round_count, strings_to_use,
//                        accidentals_to_use, tuning, start_fret, end_fret)
//                        VALUES
//                        (:id, :created_at, :host_id, :state, :round_count, :strings_to_use,
//                        :accidentals_to_use, :tuning, :start_fret, :end_fret)""")
//                    .bind("id", game.id)
//                    .bind("created_at", game.createdAt)
//                    .bind("host_id", game.hostId)
//                    .bind("state", game.getState())
//                    .bind("round_count", game.getRoundCount())
//                    .bindArray("strings_to_use", Integer.class, game.getStringsToUse().toArray())
//                    .bindArray("accidentals_to_use", String.class,
//                            game.getAccidentalsToUse()
//                                    .stream()
//                                    .map(Accidental::toString)
//                                    .toArray())
//                    .bindArray("tuning", String.class, game.getTuning())
//                    .bind("start_fret", game.getStartFret())
//                    .bind("end_fret", game.getEndFret())
//                    .execute();
//
//            if (! game.getPlayerIds().isEmpty()) {
//                updatePlayerIds(game);
//            }
//        });
    }

    public void delete(Game game)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.execute("DELETE FROM games WHERE id = ?", game.id);
        });
    }

//    public void updatePlayerIds(Game game)
//    {
//        dbConn.getJdbi().useHandle(handle -> {
//            handle.createUpdate("""
//                    UPDATE games
//                    SET player_ids = :player_ids, state = :state
//                    WHERE id = :id""")
//                    .bind("id", game.id)
//                    .bind("state", game.getState())
//                    .bindArray("player_ids", Long.class, game.getPlayerIds().toArray())
//                    .execute();
//        });
//    }

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
