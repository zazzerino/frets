package org.kdp.frets;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.io.IOException;

@QuarkusMain
public class Main
{
    public static void main(String ...args)
    {
        Quarkus.run(App.class, args);
    }

    public static class App implements QuarkusApplication
    {
        @Inject
        Logger log;

        @Inject
        DatabaseConnection dbConn;

        @Override
        public int run(String ...args)
                throws IOException
        {
            log.info("running import.sql and starting app...");
            setupDatabase();
            Quarkus.waitForExit();
            return 0;
        }

        private void setupDatabase()
                throws IOException
        {
            final var sql = Util.getResourceFileAsString("import.sql");

            dbConn.getJdbi().useHandle(handle -> {
                handle.execute(sql);
            });
        }
    }
}
