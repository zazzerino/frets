package org.kdp.frets.websocket.response.responses;

import org.kdp.frets.game.Game;
import org.kdp.frets.websocket.response.Response;

import java.util.Collection;

public class GamesResponse extends Response
{
    public final Collection<Game> games;

    public GamesResponse(Collection<Game> games)
    {
        super(Type.GAMES);
        this.games = games;
    }
}
