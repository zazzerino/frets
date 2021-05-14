package org.kdp.frets.game;

import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Timestamp;

@ApplicationScoped
public class GameDao
{
    @Inject
    DatabaseConnection dbConn;

    public void create(Game game)
    {
        dbConn.getJdbi().useHandle(handle -> {
            handle.execute(
                    "INSERT INTO games (id, created_at, state, round_count) VALUES (?, ?, ?, ?)",
                    game.id, Timestamp.from(game.createdAt), game.getState(), game.getRoundCount());
        });
    }

    public void getById(Long id)
    {
//        dbConn.getJdbi().
    }
}
