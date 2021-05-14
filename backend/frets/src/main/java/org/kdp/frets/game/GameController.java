package org.kdp.frets.game;

import org.jboss.logging.Logger;
import org.kdp.frets.user.UserDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GameController
{
    @Inject
    Logger log;

    @Inject
    UserDao userDao;

    @Inject
    GameDao gameDao;

    public void createGame(String sessionId)
    {
        final var user = userDao.getBySessionId(sessionId).orElseThrow();
        final var game = new Game();
        log.info("creating game: " + game);
        gameDao.create(game);
    }
}
