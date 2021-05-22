package org.kdp.frets.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kdp.frets.user.User;

public class GameTest
{
    @Test
    public void testRemovePlayerId()
    {
        final var user = new User("session0");
        final var game = new Game(user);

        Assertions.assertEquals(1, game.getUsers().size());
        game.removeUser(user);
        Assertions.assertTrue(game.getUsers().isEmpty());
        Assertions.assertEquals(Game.State.GAME_OVER, game.getState());
    }
}
