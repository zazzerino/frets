package org.kdp.frets.game;

import org.kdp.frets.theory.Accidental;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;

    private State state = State.INIT;
    private int roundCount = DEFAULT_ROUND_COUNT;

    private List<Integer> stringsToUse = List.of(1, 2, 3, 4, 5, 6);
    private List<Accidental> accidentalsToUse = List.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);

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

    public Game(Long id, Instant createdAt)
    {
        this.id = id;
        this.createdAt = createdAt;
    }

    public int getRoundCount()
    {
        return roundCount;
    }

    public void setRoundCount(int roundCount)
    {
        this.roundCount = roundCount;
    }

    public List<Integer> getStringsToUse()
    {
        return stringsToUse;
    }

    public void setStringsToUse(List<Integer> stringsToUse)
    {
        this.stringsToUse = stringsToUse;
    }

    public List<Accidental> getAccidentalsToUse()
    {
        return accidentalsToUse;
    }

    public void setAccidentalsToUse(List<Accidental> accidentalsToUse)
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

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", roundCount=" + roundCount +
                ", stringsToUse=" + stringsToUse +
                ", accidentalsToUse=" + accidentalsToUse +
                '}';
    }
}
