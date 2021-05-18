package org.kdp.frets.user;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User>
{
    @Override
    public User map(ResultSet rs, StatementContext ctx)
            throws SQLException
    {
        final var user = new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("session_id"));

        if (rs.getObject("game_id") != null) {
            Long gameId = rs.getLong("game_id");
            user.setGameId(gameId);
        }

        return user;
    }
}
