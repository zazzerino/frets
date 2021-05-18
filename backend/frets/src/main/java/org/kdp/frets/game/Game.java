package org.kdp.frets.game;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;
    public final Long hostId;

    private State state = State.INIT;
    private Set<Long> playerIds = new HashSet<>();

    private final static AtomicLong nextId = new AtomicLong(0);

    public enum State
    {
        INIT,
        PLAYING,
        ROUND_OVER,
        GAME_OVER
    }

    public Game(Long hostId)
    {
        id = nextId.getAndIncrement();
        createdAt = Instant.now();
        this.hostId = hostId;
        addPlayerId(hostId);
    }

    public Game(Long id, Instant createdAt, Long hostId, State state)
    {
        this.id = id;
        this.createdAt = createdAt;
        this.hostId = hostId;
        this.state = state;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public void addPlayerId(Long playerId)
    {
        playerIds.add(playerId);
    }

    public void removePlayerId(Long playerId)
    {
        playerIds.remove(playerId);

        if (playerIds.isEmpty()) {
            setState(State.GAME_OVER);
        }
    }

    public Set<Long> getPlayerIds()
    {
        return playerIds;
    }

    public void setPlayerIds(Set<Long> playerIds)
    {
        this.playerIds = playerIds;
    }

    public Long getHostId()
    {
        return hostId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id)
                // convert createdAt to Time to ignore loss of precision after saving and retrieving from postgres
                && new Time(createdAt.toEpochMilli()).equals(new Time(game.createdAt.toEpochMilli()))
                && hostId.equals(game.hostId)
                && state == game.state
                && playerIds.equals(game.playerIds);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, createdAt, hostId, state, playerIds);
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", playerIds=" + playerIds +
                '}';
    }
}

//    private int roundCount = 4;
//    private Set<Integer> stringsToUse = Set.of(1, 2, 3, 4, 5, 6);
//    private Set<Accidental> accidentalsToUse = Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);
//    private List<String> tuning = List.of("E5", "B4", "G4", "D4", "A3", "E3");
//    private int startFret = 0;
//    private int endFret = 4;
