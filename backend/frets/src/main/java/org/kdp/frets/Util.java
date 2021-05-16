package org.kdp.frets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Util
{
    /**
     * Selects a random element from an array.
     */
    public static <T> T randomElement(List<T> list)
    {
        final var index = new Random().nextInt(list.size());
        return list.get(index);
    }

    /**
     * Reads given resource file as a string.
     * https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName)
            throws IOException
    {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
