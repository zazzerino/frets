package org.kdp.frets.game;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;

import javax.inject.Inject;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(gameDao.getAll().isEmpty());

        final var user = new User("s0");
        userDao.create(user);

        final var game = new Game(user);
        gameDao.create(game);

        assertEquals(1, gameDao.getAll().size());
    }

    @Test
    @TestTransaction
    public void testDeleteGame()
    {
        final var user = new User("s0");
        final var game = new Game(user);

        gameDao.create(game);
        assertEquals(1, gameDao.getAll().size());

        gameDao.delete(game);
        assertTrue(gameDao.getAll().isEmpty());
    }

    @Test
    @TestTransaction
    public void testGetByIdAndEquals()
    {
        final var user = new User("s0");
        userDao.create(user);

        final var game = new Game(user);
        log.info("game: " + game);
        gameDao.create(game);

        user.setGameId(game.id);
        userDao.update(user);

        final var foundGame = gameDao.getById(game.id).orElseThrow();
        log.info("found game: " + foundGame);
        assertEquals(game, foundGame);
    }

    @Test
    @TestTransaction
    public void testGetAllNewest()
    {
        assertTrue(gameDao.getAll().isEmpty());

        final var user = new User("s0");
        final var game = new Game(user);
        gameDao.create(game);

        user.setGameId(game.id);
        userDao.update(user);

        assertEquals(1, gameDao.getAll().size());
        assertEquals(1, gameDao.getAllByNewest().size());
        assertEquals(Set.of(gameDao.getAll()), Set.of(gameDao.getAllByNewest()));
    }

    @Test
    @TestTransaction
    public void testAddAndRemovePlayerId()
    {
        final var user0 = new User("s0");
        userDao.create(user0);

        final var game = new Game(user0);
        gameDao.create(game);
        log.info("game: " + game);

        user0.setGameId(game.id);
        userDao.update(user0);
        log.info("user: " + user0);

        final var found = gameDao.getById(game.id).orElseThrow();
        log.info("found: " + found);

        assertEquals(1, gameDao.getById(game.id).orElseThrow().getUsers().size());

        final var user1 = new User("s1");
        user1.setGameId(game.id);
        userDao.create(user1);

        game.addUser(user1);
        gameDao.update(game);

        assertEquals(2, gameDao.getById(game.id).orElseThrow().getUsers().size());

        user1.setGameId(null);
        userDao.update(user1);

        game.removeUser(user1);
        gameDao.update(game);

        assertEquals(1, gameDao.getById(game.id).orElseThrow().getUsers().size());

        user0.setGameId(null);
        userDao.update(user0);

        game.removeUser(user0);
        gameDao.update(game);

        assertTrue(gameDao
                .getById(game.id)
                .orElseThrow()
                .getUsers()
                .isEmpty());

        assertEquals(
                Game.State.GAME_OVER,
                gameDao.getById(game.id)
                        .orElseThrow()
                        .getState());
    }
}
