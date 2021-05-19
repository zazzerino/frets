package org.kdp.frets.user;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class User
{
    public final Long id;
    public final String sessionId;

    private String name = DEFAULT_NAME;
    private Long gameId;

    public static final String DEFAULT_NAME = "anon";
    private static final AtomicLong nextId = new AtomicLong(0);

    public User(String sessionId)
    {
        id = nextId.getAndIncrement();
        this.sessionId = sessionId;
    }

    public User(Long id, String name, String sessionId)
    {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    public User(Long id, String name, String sessionId, Long gameId)
    {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
        this.gameId = gameId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getGameId()
    {
        return gameId;
    }

    public void setGameId(Long gameId)
    {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && sessionId.equals(user.sessionId) && name.equals(user.name) && Objects.equals(gameId, user.gameId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, sessionId, name, gameId);
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
