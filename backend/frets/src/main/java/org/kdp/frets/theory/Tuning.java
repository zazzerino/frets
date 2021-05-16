package org.kdp.frets.theory;

import java.util.List;

/**
 * This class represents the tuning of a stringed instrument.
 * It contains a list of notes that each string is tuned to
 * starting from the highest-pitched string (string 1) to the lowest.
 */
public record Tuning(List<String> notes)
{
    public static final Tuning STANDARD_GUITAR = new Tuning(
            List.of("E5", "B4", "G4", "D4", "A3", "E3"));

    public String get(int index)
    {
        return notes.get(index);
    }

    public int size()
    {
        return notes.size();
    }
}
