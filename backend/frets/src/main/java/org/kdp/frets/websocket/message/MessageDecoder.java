package org.kdp.frets.websocket.message;

import io.vertx.core.json.JsonObject;
import org.kdp.frets.websocket.message.messages.CreateGameMessage;
import org.kdp.frets.websocket.message.messages.JoinGameMessage;
import org.kdp.frets.websocket.message.messages.LoginMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message>
{
    @Override
    public Message decode(String s) throws DecodeException
    {
        final var json = new JsonObject(s);
        final var msgType = getMessageType(json);

        switch (msgType)
        {
            case LOGIN -> {
                final var name = json.getString("name");
                return new LoginMessage(name);
            }
            case CREATE_GAME -> {
                return new CreateGameMessage();
            }
            case JOIN_GAME -> {
                final var gameId = json.getLong("gameId");
                final var userId = json.getLong("userId");
                return new JoinGameMessage(gameId, userId);
            }
            default -> throw new DecodeException(s, "error decoding json");
        }
    }

    @Override
    public boolean willDecode(String s)
    {
        final var json = new JsonObject(s);
        return getMessageType(json) != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig)
    {
    }

    @Override
    public void destroy()
    {
    }

    private Message.Type getMessageType(JsonObject json)
    {
        final var type = json.getString("type");
        return Message.Type.valueOf(type);
    }
}
