package org.kdp.frets.user;

import java.util.concurrent.atomic.AtomicLong;

public record User(Long id,
                   String name,
                   String sessionId)
{
    public static final String DEFAULT_NAME = "anon";

    private static final AtomicLong nextId = new AtomicLong(0);

    public User(String sessionId)
    {
        this(nextId.getAndIncrement(), DEFAULT_NAME, sessionId);
    }

    public User withName(String name)
    {
        return new User(id, name, sessionId);
    }
}
