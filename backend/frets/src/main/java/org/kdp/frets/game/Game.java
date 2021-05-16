package org.kdp.frets.game;

import org.kdp.frets.theory.Accidental;
import org.kdp.frets.theory.Fretboard;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;
    public final Long hostId;

    private State state = State.INIT;
    private Set<Long> playerIds = new HashSet<>();

    private int roundCount = DEFAULT_ROUND_COUNT;
    private Set<Integer> stringsToUse = Set.of(1, 2, 3, 4, 5, 6);
    private Set<Accidental> accidentalsToUse = Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);

    private final static AtomicLong nextId = new AtomicLong(0);
    private final static int DEFAULT_ROUND_COUNT = 4;

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
    }

    public Game(Long id, Instant createdAt, Long hostId)
    {
        this.id = id;
        this.createdAt = createdAt;
        this.hostId = hostId;
    }

    public void addPlayerId(Long playerId)
    {
        playerIds.add(playerId);
    }

    public void removePlayerId(long playerId)
    {
        playerIds.remove(playerId);

        if (playerIds.isEmpty()) {
            state = State.GAME_OVER;
        }
    }

    public int getRoundCount()
    {
        return roundCount;
    }

    public void setRoundCount(int roundCount)
    {
        this.roundCount = roundCount;
    }

    public Set<Integer> getStringsToUse()
    {
        return stringsToUse;
    }

    public void setStringsToUse(Set<Integer> stringsToUse)
    {
        this.stringsToUse = stringsToUse;
    }

    public Set<Accidental> getAccidentalsToUse()
    {
        return accidentalsToUse;
    }

    public void setAccidentalsToUse(Set<Accidental> accidentalsToUse)
    {
        this.accidentalsToUse = accidentalsToUse;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public Set<Long> getPlayerIds()
    {
        return playerIds;
    }

    public void setPlayerIds(Set<Long> playerIds)
    {
        this.playerIds = playerIds;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", playerIds=" + playerIds +
                ", roundCount=" + roundCount +
                ", stringsToUse=" + stringsToUse +
                ", accidentalsToUse=" + accidentalsToUse +
                '}';
    }
}
