package org.kdp.frets.game;

import org.kdp.frets.theory.Accidental;
import org.kdp.frets.user.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;
    public final Long hostId;

    private State state = State.INIT;
    private final Set<User> players = new HashSet<>();

//    private int roundCount = 4;
//    private Set<Integer> stringsToUse = Set.of(1, 2, 3, 4, 5, 6);
//    private Set<Accidental> accidentalsToUse = Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);
//    private List<String> tuning = List.of("E5", "B4", "G4", "D4", "A3", "E3");
//    private int startFret = 0;
//    private int endFret = 4;

    private final static AtomicLong nextId = new AtomicLong(0);

    public enum State
    {
        INIT,
        PLAYING,
        ROUND_OVER,
        GAME_OVER
    }

    public Game(User host)
    {
        id = nextId.getAndIncrement();
        createdAt = Instant.now();
        hostId = host.id;
        addPlayer(host);
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

    public void addPlayer(User user)
    {
        players.add(user);
    }

    public void removePlayer(User user)
    {
        players.remove(user);

        if (players.isEmpty()) {
            setState(State.GAME_OVER);
        }
    }

    public Set<User> getPlayers()
    {
        return players;
    }

//    public int getRoundCount()
//    {
//        return roundCount;
//    }
//
//    public void setRoundCount(int roundCount)
//    {
//        this.roundCount = roundCount;
//    }
//
//    public Set<Integer> getStringsToUse()
//    {
//        return stringsToUse;
//    }
//
//    public void setStringsToUse(Set<Integer> stringsToUse)
//    {
//        this.stringsToUse = stringsToUse;
//    }
//
//    public Set<Accidental> getAccidentalsToUse()
//    {
//        return accidentalsToUse;
//    }
//
//    public void setAccidentalsToUse(Set<Accidental> accidentalsToUse)
//    {
//        this.accidentalsToUse = accidentalsToUse;
//    }

//    public List<String> getTuning()
//    {
//        return tuning;
//    }
//
//    public void setTuning(List<String> tuning)
//    {
//        this.tuning = tuning;
//    }
//
//    public int getStartFret()
//    {
//        return startFret;
//    }
//
//    public void setStartFret(int startFret)
//    {
//        this.startFret = startFret;
//    }
//
//    public int getEndFret()
//    {
//        return endFret;
//    }
//
//    public void setEndFret(int endFret)
//    {
//        this.endFret = endFret;
//    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", players=" + players +
//                ", roundCount=" + roundCount +
//                ", stringsToUse=" + stringsToUse +
//                ", accidentalsToUse=" + accidentalsToUse +
                '}';
    }
}
