package org.kdp.frets.websocket.message.messages;

import org.kdp.frets.websocket.message.Message;

public class JoinGameMessage extends Message
{
    public final Long gameId;
    public final Long userId;

    public JoinGameMessage(Long gameId, Long userId)
    {
        super(Type.JOIN_GAME);
        this.gameId = gameId;
        this.userId = userId;
    }
}
