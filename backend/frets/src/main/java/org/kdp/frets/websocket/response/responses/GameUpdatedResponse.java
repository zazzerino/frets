package org.kdp.frets.websocket.response.responses;

import org.kdp.frets.game.Game;
import org.kdp.frets.websocket.response.Response;

public class GameUpdatedResponse extends Response
{
    public final Game game;

    public GameUpdatedResponse(Game game)
    {
        super(Type.GAME_UPDATED);
        this.game = game;
    }
}
