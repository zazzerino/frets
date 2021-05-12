package org.kdp.frets.user;

import org.jboss.logging.Logger;
import org.kdp.frets.DatabaseConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserDao
{
    @Inject
    Logger log;

    @Inject
    DatabaseConnection dbConn;

    @Transactional
    public void save(User user)
    {
        log.info("saving user: " + user);
        log.info(dbConn.getJdbi());
    }
}
