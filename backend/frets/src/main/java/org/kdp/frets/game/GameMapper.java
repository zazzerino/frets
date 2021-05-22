package org.kdp.frets.game;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

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

            return new Game(id, createdAt, hostId, state);
    }
}
