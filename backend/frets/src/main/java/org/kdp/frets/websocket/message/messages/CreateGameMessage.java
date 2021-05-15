package org.kdp.frets.websocket.message.messages;

import org.kdp.frets.websocket.message.Message;

public class CreateGameMessage extends Message
{
    public CreateGameMessage()
    {
        super(Type.CREATE_GAME);
    }
}
