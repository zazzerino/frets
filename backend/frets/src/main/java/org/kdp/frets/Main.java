package org.kdp.frets;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

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
        DatabaseConnection dbConn;

        @Override
        public int run(String ...args)
                throws IOException
        {
            System.out.println("running app..");

            final var sql = Util.getResourceFileAsString("import.sql");

            dbConn.getJdbi().useHandle(handle -> {
                handle.execute(sql);
            });

            Quarkus.waitForExit();
            return 0;
        }
    }
}
