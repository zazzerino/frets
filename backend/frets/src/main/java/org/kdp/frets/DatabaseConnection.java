package org.kdp.frets;

import io.agroal.api.AgroalDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.kdp.frets.game.Game;
import org.kdp.frets.theory.Accidental;
import org.kdp.frets.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DatabaseConnection
{
    @Inject
    AgroalDataSource dataSource;

    private Jdbi jdbi;

    public Jdbi getJdbi()
    {
        if (jdbi == null) {
            jdbi = createJdbi();
        }

        return jdbi;
    }

    private Jdbi createJdbi()
    {
        return Jdbi.create(dataSource)
                .installPlugin(new PostgresPlugin())
                .registerRowMapper(User.class, this::userRowMapper)
                .registerRowMapper(Game.class, this::gameRowMapper);
    }

    private User userRowMapper(ResultSet rs, StatementContext ctx)
            throws SQLException
    {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("session_id"));
    }

    private Game gameRowMapper(ResultSet rs, StatementContext ctx)
            throws SQLException
    {
        try {
            final var id = rs.getLong("id");
            final var createdAt = rs.getTimestamp("created_at").toInstant();

            final var state = Game.State.valueOf(rs.getString("state"));
            final var roundCount = rs.getInt("round_count");

            final var stringsArray = (Integer[]) rs.getArray("strings_to_use").getArray();
            final var stringsToUse = Arrays.stream(stringsArray).collect(Collectors.toSet());

            final var accidentalsArray = (String[]) rs.getArray("accidentals_to_use").getArray();
            final var accidentalsToUse = Arrays.stream(accidentalsArray)
                    .map(Accidental::valueOf)
                    .collect(Collectors.toSet());

            final var playerArray = rs.getArray("player_ids");
            Set<Long> playerIds = null;

            if (playerArray != null) {
                final var intArray = (Integer[]) playerArray.getArray();
                playerIds = Arrays.stream(intArray)
                        .map(Integer::longValue)
                        .collect(Collectors.toSet());
            }

            final var hostId = rs.getLong("host_id");

            final var game = new Game(id, createdAt);

            game.setState(state);
            game.setRoundCount(roundCount);
            game.setStringsToUse(stringsToUse);
            game.setAccidentalsToUse(accidentalsToUse);
            game.setHostId(hostId);

            if (playerIds != null) {
                game.setPlayerIds(playerIds);
            }

            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
