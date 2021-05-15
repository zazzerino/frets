package org.kdp.frets.game;

import org.kdp.frets.DatabaseConnection;
import org.kdp.frets.theory.Accidental;
import org.kdp.frets.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GameDao
{
    @Inject
    DatabaseConnection dbConn;

    public Optional<Game> getById(Long gameId)
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM games WHERE id = ?", gameId)
                        .mapTo(Game.class)
                        .findFirst());
    }

    public List<Game> getAll()
    {
        return dbConn.getJdbi()
                .withHandle(handle -> handle
                        .select("SELECT * FROM games")
                        .mapTo(Game.class)
                        .list());
    }

    public void create(Game game)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.createUpdate("""
                    INSERT INTO games (id, created_at, state, round_count, strings_to_use, accidentals_to_use)
                    VALUES (:id, :created_at, :state, :round_count, :strings_to_use, :accidentals_to_use)""")
                    .bind("id", game.id)
                    .bind("created_at", game.createdAt)
                    .bind("state", game.getState())
                    .bind("round_count", game.getRoundCount())
                    .bindArray("strings_to_use", Integer.class, game.getStringsToUse().toArray())
                    .bindArray("accidentals_to_use", String.class,
                            game.getAccidentalsToUse()
                                    .stream()
                                    .map(Accidental::toString)
                                    .toArray())
                    .execute();
        });
    }

    public void addPlayerToGame(Game game, User user)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.execute("""
                    INSERT INTO game_users (game_id, user_id)
                    VALUES (?, ?)""",
                    game.id, user.id);
        });
    }
}
