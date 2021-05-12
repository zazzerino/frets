package org.kdp.frets;

import io.agroal.api.AgroalDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DatabaseConnection
{
    @Inject
    AgroalDataSource dataSource;

    private Jdbi jdbi;

    public Jdbi getJdbi()
    {
        if (jdbi == null) {
            jdbi = Jdbi.create(dataSource)
                    .installPlugin(new PostgresPlugin());
        }

        return jdbi;
    }
}
