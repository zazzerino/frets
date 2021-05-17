package org.kdp.frets.game;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;

import javax.inject.Inject;

@QuarkusTest
public class GameDaoTest
{
    @Inject
    Logger log;

    @Inject
    GameDao gameDao;

    @Inject
    UserDao userDao;

    @Test
    @TestTransaction
    public void testCreateGame()
    {
        final var user = new User("session0");
        final var game = new Game(user);
        user.setGameId(game.id);
        gameDao.create(game);
        userDao.create(user);
        log.info("game: " + game);

        final var foundGame = gameDao.getById(game.id);
        log.info("foundGame: " + foundGame);
    }
}
