package org.kdp.frets.game;

import org.kdp.frets.user.User;

import java.sql.Time;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Game
{
    public final Long id;
    public final Instant createdAt;

    private Long hostId;
    private State state = State.INIT;
    private Set<User> users = new HashSet<>();

    private final static AtomicLong nextId = new AtomicLong(0);

    public enum State
    {
        INIT,
        PLAYING,
        ROUND_OVER,
        GAME_OVER
    }

    public Game(User user)
    {
        id = nextId.getAndIncrement();
        createdAt = Instant.now();

        hostId = user.id;
        addUser(user);
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

    public Long getHostId()
    {
        return hostId;
    }

    public void setHostId(Long hostId)
    {
        this.hostId = hostId;
    }

    public void addUser(User user)
    {
        users.add(user);
    }

    public void removeUser(User user)
    {
        users.remove(user);

        if (users.isEmpty()) {
            setState(State.GAME_OVER);
        }
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id)
                // ignore loss of precision after saving and retrieving from postgres
                && new Time(createdAt.toEpochMilli()).equals(new Time(game.createdAt.toEpochMilli()))
                && hostId.equals(game.hostId)
                && state == game.state
                && users.equals(game.users);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, createdAt, hostId, state, users);
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", state=" + state +
                ", users=" + users +
                '}';
    }
}

//    private int roundCount = 4;
//    private Set<Integer> stringsToUse = Set.of(1, 2, 3, 4, 5, 6);
//    private Set<Accidental> accidentalsToUse = Set.of(Accidental.FLAT, Accidental.NONE, Accidental.SHARP);
//    private List<String> tuning = List.of("E5", "B4", "G4", "D4", "A3", "E3");
//    private int startFret = 0;
//    private int endFret = 4;
