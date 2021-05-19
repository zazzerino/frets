package org.kdp.frets;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class LifecycleObserver
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    void onStart(@Observes StartupEvent event)
    {
        log.info("setting up database");
        setupDatabase();
    }

    void onStop(@Observes ShutdownEvent event)
    {
        log.info("goodbye");
    }

    private void setupDatabase()
    {
        try {
            final var importSql = Util.getResourceFileAsString("import.sql");

            dbConn.getJdbi().useHandle(handle -> {
                handle.execute(importSql);
            });
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }
}
