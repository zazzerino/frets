package org.kdp.frets.user;

import java.util.Optional;

public record User(Long id,
                   String name,
                   String sessionId,
                   Optional<Long> gameId)
{
    public User(Long id, String name, String sessionId)
    {
        this(id, name, sessionId, Optional.empty());
    }
}
