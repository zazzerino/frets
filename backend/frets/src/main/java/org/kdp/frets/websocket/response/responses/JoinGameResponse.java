package org.kdp.frets.websocket.response.responses;

import org.kdp.frets.game.Game;
import org.kdp.frets.websocket.response.Response;

public class JoinGameResponse extends Response
{
    public final Game game;

    public JoinGameResponse(Game game)
    {
        super(Type.JOIN_GAME);
        this.game = game;
    }
}
