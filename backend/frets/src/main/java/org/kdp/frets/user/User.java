package org.kdp.frets.user;

import java.util.concurrent.atomic.AtomicLong;

public class User
{
    public final Long id;
    public final String sessionId;

    private String name;

    public static final String DEFAULT_NAME = "anon";
    private static final AtomicLong nextId = new AtomicLong(0);

    public User(Long id, String name, String sessionId)
    {
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
    }

    public User(String sessionId)
    {
        id = nextId.getAndIncrement();
        name = DEFAULT_NAME;
        this.sessionId = sessionId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
