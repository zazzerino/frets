package org.kdp.frets.theory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a slice of a fretboard from `startFret` to `endFret` in the given `tuning`.
 * The `notes` field maps each Fretboard.Coord to the note found at that Coord.
 */
public record Fretboard(Tuning tuning,
                        int startFret,
                        int endFret,
                        Map<Coord, Note> notes)
{
    /**
     * The 1st position of a 6-string guitar in standard tuning.
     */
    public static Fretboard DEFAULT = Fretboard.create(Tuning.STANDARD_GUITAR, 0, 4);

    /**
     * @param startFret the lowest fret (in pitch & number)
     * @param endFret   the highest fret (in pitch & number)
     * @return a new Fretboard and calculates the notes on that can be found on that fretboard
     */
    public static Fretboard create(Tuning tuning, int startFret, int endFret)
    {
        final var notes = calculateNotes(tuning, startFret, endFret);
        return new Fretboard(tuning, startFret, endFret, notes);
    }

    /**
     * @return a Map with the keys being each Fretboard.Coord and the values being the Notes played at that coord
     */
    public static Map<Coord, Note> calculateNotes(Tuning tuning, int startFret, int endFret)
    {
        final Map<Coord, Note> notes = new HashMap<>();
        final var stringCount = tuning.notes().size();

        for (var string = 0; string < stringCount; string++) {
            for (var fret = endFret; fret >= startFret; fret--) {
                final var coord = new Coord(string + 1, fret);
                final var openNote = Note.from(tuning.get(string));

                // transpose the open string note up `fret` number of half-steps
                final var note = openNote.transpose(fret);
                notes.put(coord, note);
            }
        }

        return notes;
    }

    /**
     * @return the Note at the given Fretboard.Coord (string & fret)
     */
    public Optional<Note> findNoteAt(Coord coord)
    {
        return Optional.ofNullable(notes.get(coord));
    }

    /**
     * @return the Fretboard.Coord where a given Note is played.
     */
    public Optional<Coord> findCoord(Note note)
    {
        return notes.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isEnharmonicWith(note))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    /**
     * @return the number of frets on the Fretboard
     */
    public int fretCount()
    {
        return endFret - startFret;
    }

    public int stringCount()
    {
        return tuning.size();
    }

    /**
     * @return a random note on that can be played on the fretboard.
     */
    public Note randomNote()
    {
        final var lowNote = Note.from(tuning.get(tuning.size() - 1));
        final var highNote = Note.from(tuning.get(0)).transpose(fretCount());

        return Note.randomBetween(lowNote, highNote);
    }

    public List<Note> notesOnString(int string)
    {
        if (string < 1 || string > stringCount() + 1) {
            throw new IllegalArgumentException();
        }

        final var notes = new ArrayList<Note>();

        for (var fret = startFret; fret <= endFret; fret++) {
            final var coord = new Fretboard.Coord(string, fret);
            final var note = findNoteAt(coord).orElseThrow();

            notes.add(note);
        }

        return notes;
    }

    /**
     * Fretboard.Coord represents a location on the fretboard (the string & fret).
     */
    public static record Coord(int string, int fret)
    {
    }
}
