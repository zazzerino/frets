package org.kdp.frets.websocket.message;

public class CreateGameMessage extends Message
{
    public CreateGameMessage()
    {
        super(Type.CREATE_GAME);
    }
}
