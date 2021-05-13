package org.kdp.frets;

import io.agroal.api.AgroalDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.kdp.frets.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

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
                .registerRowMapper(User.class, (rs, ctx) -> new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("session_id"),
                        Optional.of(rs.getLong("game_id"))));
    }
}
