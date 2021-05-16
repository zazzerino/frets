package org.kdp.frets.theory;

public enum Octave
{
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    private static final Octave[] vals = values();

    public final int val;

    Octave(int val)
    {
        this.val = val;
    }

    public static Octave from(String num)
    {
        return switch (num) {
            case "1" -> ONE;
            case "2" -> TWO;
            case "3" -> THREE;
            case "4" -> FOUR;
            case "5" -> FIVE;
            case "6" -> SIX;
            case "7" -> SEVEN;
            case "8" -> EIGHT;
            case "9" -> NINE;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public Octave next()
    {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
