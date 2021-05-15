package org.kdp.frets.websocket.response.responses;

import org.kdp.frets.user.User;
import org.kdp.frets.websocket.response.Response;

public class LoginResponse extends Response
{
    public final User user;

    public LoginResponse(User user)
    {
        super(Type.LOGIN);
        this.user = user;
    }
}
