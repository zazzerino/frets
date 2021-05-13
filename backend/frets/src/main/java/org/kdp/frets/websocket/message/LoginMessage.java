package org.kdp.frets.websocket.message;

public class LoginMessage extends Message
{
    public final String name;

    public LoginMessage(String name)
    {
        super(Type.LOGIN);
        this.name = name;
    }
}
