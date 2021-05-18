package org.kdp.frets.game;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kdp.frets.user.User;

import javax.inject.Inject;
import java.util.Set;

@QuarkusTest
public class GameDaoTest
{
    @Inject
    GameDao gameDao;

    @Test
    @TestTransaction
    public void testCreateGame()
    {
        Assertions.assertTrue(gameDao.getAll().isEmpty());

        final var game = new Game(0L);
        gameDao.create(game);

        Assertions.assertEquals(1, gameDao.getAll().size());
    }

    @Test
    @TestTransaction
    public void testDeleteGame()
    {
        final var game = new Game(0L);
        gameDao.create(game);
        Assertions.assertEquals(1, gameDao.getAll().size());
        gameDao.delete(game);
        Assertions.assertTrue(gameDao.getAll().isEmpty());
    }

    @Test
    @TestTransaction
    public void testGetByIdAndEquals()
    {
        final var game = new Game(0L);
        gameDao.create(game);

        final var foundGame = gameDao.getById(game.id).orElseThrow();
        Assertions.assertEquals(game, foundGame);
    }

    @Test
    @TestTransaction
    public void testGetAllNewest()
    {
        Assertions.assertTrue(gameDao.getAll().isEmpty());

        final var game = new Game(0L);
        gameDao.create(game);

        Assertions.assertEquals(1, gameDao.getAll().size());
        Assertions.assertEquals(1, gameDao.getAllByNewest().size());
        Assertions.assertEquals(Set.of(gameDao.getAll()), Set.of(gameDao.getAllByNewest()));
    }

    @Test
    @TestTransaction
    public void testAddAndRemovePlayerId()
    {
        final var user0 = new User("s0");
        final var user1 = new User("s1");

        final var game = new Game(user0.id);
        gameDao.create(game);

        Assertions.assertEquals(
                1,
                gameDao.getById(game.id).orElseThrow().getPlayerIds().size());

        game.addPlayerId(user1.id);
        gameDao.updatePlayerIdsAndState(game);

        Assertions.assertEquals(
                2,
                gameDao.getById(game.id).orElseThrow().getPlayerIds().size());

        game.removePlayerId(user1.id);
        gameDao.updatePlayerIdsAndState(game);
        Assertions.assertEquals(
                1,
                gameDao.getById(game.id).orElseThrow().getPlayerIds().size());

        game.removePlayerId(user0.id);
        gameDao.updatePlayerIdsAndState(game);

        Assertions.assertTrue(gameDao
                .getById(game.id)
                .orElseThrow()
                .getPlayerIds()
                .isEmpty());

        Assertions.assertEquals(
                Game.State.GAME_OVER,
                gameDao.getById(game.id)
                        .orElseThrow()
                        .getState());
    }
}

//        final var user = new User("session0");
//        final var game = new Game(user);
//        user.setGameId(game.id);
//        gameDao.create(game);
//        userDao.create(user);
//        log.info("game: " + game);
//
//        final var foundGame = gameDao.getById(game.id);
//        log.info("foundGame: " + foundGame);
