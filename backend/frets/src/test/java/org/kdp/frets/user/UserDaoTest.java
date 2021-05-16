package org.kdp.frets.user;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class UserDaoTest
{
    @Inject
    UserDao userDao;

    @Test
    @TestTransaction
    public void testCreateUser()
    {
        Assertions.assertTrue(userDao.getAll().isEmpty());

        final var user = new User("session0");
        userDao.create(user);

        Assertions.assertEquals(1, userDao.getAll().size());

        final var foundUser = userDao.getById(user.id).orElseThrow();

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.id, foundUser.id);
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.sessionId, foundUser.sessionId);
        Assertions.assertEquals(user, foundUser);
    }
}
