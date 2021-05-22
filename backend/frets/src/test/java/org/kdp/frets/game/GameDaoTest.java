package org.kdp.frets.game;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.kdp.frets.user.User;
import org.kdp.frets.user.UserDao;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class GameDaoTest
{
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

//    @Test
//    @TestTransaction
//    public void testDeleteGame()
//    {
//        final var user = new User("s0");
//        final var game = new Game(user);
//        gameDao.create(game);
//        assertEquals(1, gameDao.getAll().size());
//        gameDao.delete(game);
//        assertTrue(gameDao.getAll().isEmpty());
//    }
//
//    @Test
//    @TestTransaction
//    public void testGetByIdAndEquals()
//    {
//        final var user = new User("s0");
//        final var game = new Game(user);
//        gameDao.create(game);
//
//        final var foundGame = gameDao.getById(game.id).orElseThrow();
//        assertEquals(game, foundGame);
//    }
//
//    @Test
//    @TestTransaction
//    public void testGetAllNewest()
//    {
//        assertTrue(gameDao.getAll().isEmpty());
//
//        final var user = new User("s0");
//        final var game = new Game(user);
//        gameDao.create(game);
//
//        assertEquals(1, gameDao.getAll().size());
//        assertEquals(1, gameDao.getAllByNewest().size());
//        assertEquals(Set.of(gameDao.getAll()), Set.of(gameDao.getAllByNewest()));
//    }
//
//    @Test
//    @TestTransaction
//    public void testAddAndRemovePlayerId()
//    {
//        final var user0 = new User("s0");
//        final var user1 = new User("s1");
//
//        final var game = new Game(user0);
//        gameDao.create(game);
//
//        assertEquals(
//                1,
//                gameDao.getById(game.id).orElseThrow().getUsers().size());
//
//        game.addUser(user1);
//        gameDao.updatePlayerIdsAndState(game);
//
//        assertEquals(
//                2,
//                gameDao.getById(game.id).orElseThrow().getUsers().size());
//
//        game.removeUser(user1);
//        gameDao.updatePlayerIdsAndState(game);
//
//        assertEquals(
//                1,
//                gameDao.getById(game.id).orElseThrow().getUsers().size());
//
//        game.removeUser(user0);
//        gameDao.updatePlayerIdsAndState(game);
//
//        assertTrue(gameDao
//                .getById(game.id)
//                .orElseThrow()
//                .getUsers()
//                .isEmpty());
//
//        assertEquals(
//                Game.State.GAME_OVER,
//                gameDao.getById(game.id)
//                        .orElseThrow()
//                        .getState());
//    }
}
