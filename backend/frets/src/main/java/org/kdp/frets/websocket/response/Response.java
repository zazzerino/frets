package org.kdp.frets.websocket.response;

public class Response
{
    public enum Type
    {
        LOGIN,
        GAMES,
        CREATE_GAME,
        JOIN_GAME
    }

    public final Type type;

    public Response(Type type)
    {
        this.type = type;
    }
}
