package org.kdp.frets.websocket.message.messages;

import org.kdp.frets.websocket.message.Message;

public class LoginMessage extends Message
{
    public final String name;

    public LoginMessage(String name)
    {
        super(Type.LOGIN);
        this.name = name;
    }
}
