package org.kdp.frets.theory;

public enum Accidental
{
    DOUBLE_FLAT,
    FLAT,
    NONE,
    NATURAL,
    SHARP,
    DOUBLE_SHARP;

    public int halfStepOffset()
    {
        return switch (this) {
            case DOUBLE_FLAT -> -2;
            case FLAT -> -1;
            case NONE, NATURAL -> 0;
            case SHARP -> 1;
            case DOUBLE_SHARP -> 2;
        };
    }

    public Accidental next()
    {
        return switch (this) {
            case DOUBLE_FLAT -> FLAT;
            case FLAT, SHARP -> NONE;
            case NONE, NATURAL, DOUBLE_SHARP -> SHARP;
        };
    }
}
