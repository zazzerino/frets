package org.kdp.frets;

public enum Accidental
{
    DOUBLE_FLAT("bb"),
    FLAT("b"),
    NONE(""),
    NATURAL("n"),
    SHARP("#"),
    DOUBLE_SHARP("##");

    public final String val;

    Accidental(String val)
    {
        this.val = val;
    }

    public static Accidental from(String val)
    {
        return switch (val.toLowerCase()) {
            case "bb" -> DOUBLE_FLAT;
            case "b" -> FLAT;
            case "" -> NONE;
            case "n" -> NATURAL;
            case "#" -> SHARP;
            case "##" -> DOUBLE_SHARP;
            default -> throw new IllegalStateException("Unexpected value: " + val);
        };
    }

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
