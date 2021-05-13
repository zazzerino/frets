package org.kdp.frets.websocket.response;

import org.kdp.frets.user.User;

public class LoginResponse extends Response
{
    public final User user;

    public LoginResponse(User user)
    {
        super(Type.LOGIN);
        this.user = user;
    }
}
