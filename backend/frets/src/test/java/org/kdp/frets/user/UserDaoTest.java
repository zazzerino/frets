package org.kdp.frets.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserDaoTest
{
    @Inject
    UserDao userDao;

    @Test
    @TestTransaction
    public void testCreateAndFindUser()
    {
        assertTrue(userDao.getAll().isEmpty());

        final var user = new User("session0");
        userDao.create(user);

        assertEquals(1, userDao.getAll().size());

        final var foundUser = userDao.getById(user.id).orElseThrow();

        assertNotNull(foundUser);
        assertEquals(user.id, foundUser.id);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.sessionId, foundUser.sessionId);
        assertEquals(user, foundUser);
    }

    @Test
    @TestTransaction
    public void testDeleteUser()
    {
        assertTrue(userDao.getAll().isEmpty());

        var user = new User("session0");
        userDao.create(user);

        assertEquals(1, userDao.getAll().size());

        userDao.delete(user);

        assertTrue(userDao.getAll().isEmpty());
    }

    @Test
    @TestTransaction
    public void testUpdateName()
    {
        final var name = "Alice";
        final var user = new User("session0");
        userDao.create(user);
        assertEquals(User.DEFAULT_NAME, user.getName());
        user.setName(name);
        userDao.updateName(user);

        final var foundUser = userDao.getById(user.id).orElseThrow();
        assertEquals(name, foundUser.getName());
    }

//    @Test
//    @TestTransaction
//    public void testUpdateGameId()
//    {
//        final var gameId = 42L;
//
//        var user = new User("session0");
//        user.setGameId(gameId);
//        userDao.create(user);
//
//        user = userDao.getById(user.id).orElseThrow();
//        assertEquals(gameId, user.getGameId());
//
//        user.setGameId(null);
//        userDao.updateGameId(user);
//
//        user = userDao.getById(user.id).orElseThrow();
//        assertNull(user.getGameId());
//    }
}
