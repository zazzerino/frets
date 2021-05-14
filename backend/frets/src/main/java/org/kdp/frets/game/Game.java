package org.kdp.frets.game;

import org.kdp.frets.Accidental;
import org.kdp.frets.user.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;

    private State state = State.INIT;
    private int roundCount = DEFAULT_ROUND_COUNT;
    private final List<User> players = new ArrayList<>();
    private final Set<Integer> stringsToUse = Set.of(1, 2, 3, 4, 5, 6);;
    private final Set<Accidental> accidentalsToUse = Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);;

    private final static AtomicLong nextId = new AtomicLong(0);
    private final static int DEFAULT_ROUND_COUNT = 4;

    public enum State
    {
        INIT,
        PLAYING,
        ROUND_OVER,
        GAME_OVER
    }

    public Game()
    {
        id = nextId.getAndIncrement();
        createdAt = Instant.now();
    }

    public Game(Long id, Instant createdAt, State state, int roundCount)
    {
        this.id = id;
        this.createdAt = createdAt;
        this.state = state;
        this.roundCount = roundCount;
    }

    public List<User> getPlayers()
    {
        return players;
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

    public Set<Accidental> getAccidentalsToUse()
    {
        return accidentalsToUse;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", roundCount=" + roundCount +
                ", players=" + players +
                ", stringsToUse=" + stringsToUse +
                ", accidentalsToUse=" + accidentalsToUse +
                '}';
    }
}
