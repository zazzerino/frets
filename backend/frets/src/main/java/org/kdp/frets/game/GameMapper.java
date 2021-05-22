package org.kdp.frets.game;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

public class GameMapper implements RowMapper<Game>
{
    @Override
    public Game map(ResultSet rs, StatementContext ctx)
            throws SQLException
    {
            final var id = rs.getLong("id");
            final var createdAt = rs.getTimestamp("created_at").toInstant();
            final var hostId = rs.getLong("host_id");
            final var state = Game.State.valueOf(rs.getString("state"));

            final var game = new Game(id, createdAt, hostId, state);

//            if (rs.getObject("player_ids") != null) {
//                final var playerIds = (Long[]) rs.getArray("player_ids").getArray();
//                game.setPlayerIds(new HashSet<>(Arrays.asList(playerIds)));
//            }

            return game;
    }
}
