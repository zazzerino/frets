package org.kdp.frets.websocket.message;

public class Message
{
    public enum Type
    {
        LOGIN,
        CREATE_GAME,
        JOIN_GAME
    }

    public final Type type;

    public Message(Type type)
    {
        this.type = type;
    }
}
