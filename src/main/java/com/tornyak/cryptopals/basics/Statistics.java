package com.tornyak.cryptopals.basics;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Statistics {

    private static final Map<Character, Double> ENGLISH_CHAR_FREQUENCY = new HashMap<>();

    static {
        ENGLISH_CHAR_FREQUENCY.put('a', 8.167);
        ENGLISH_CHAR_FREQUENCY.put('b', 1.492);
        ENGLISH_CHAR_FREQUENCY.put('c', 2.202);
        ENGLISH_CHAR_FREQUENCY.put('d', 4.253);
        ENGLISH_CHAR_FREQUENCY.put('e', 12.702);
        ENGLISH_CHAR_FREQUENCY.put('f', 2.228);
        ENGLISH_CHAR_FREQUENCY.put('g', 2.015);
        ENGLISH_CHAR_FREQUENCY.put('h', 6.094);
        ENGLISH_CHAR_FREQUENCY.put('i', 6.966);
        ENGLISH_CHAR_FREQUENCY.put('j', 0.153);
        ENGLISH_CHAR_FREQUENCY.put('k', 1.292);
        ENGLISH_CHAR_FREQUENCY.put('l', 4.025);
        ENGLISH_CHAR_FREQUENCY.put('m', 2.406);
        ENGLISH_CHAR_FREQUENCY.put('n', 6.749);
        ENGLISH_CHAR_FREQUENCY.put('o', 7.507);
        ENGLISH_CHAR_FREQUENCY.put('p', 1.929);
        ENGLISH_CHAR_FREQUENCY.put('q', 0.095);
        ENGLISH_CHAR_FREQUENCY.put('r', 5.987);
        ENGLISH_CHAR_FREQUENCY.put('s', 6.327);
        ENGLISH_CHAR_FREQUENCY.put('t', 9.356);
        ENGLISH_CHAR_FREQUENCY.put('u', 2.758);
        ENGLISH_CHAR_FREQUENCY.put('v', 0.978);
        ENGLISH_CHAR_FREQUENCY.put('w', 2.560);
        ENGLISH_CHAR_FREQUENCY.put('x', 0.150);
        ENGLISH_CHAR_FREQUENCY.put('y', 1.994);
        ENGLISH_CHAR_FREQUENCY.put('z', 0.077);
    }

    private Statistics(){}

    /**
     * Method will calculate frequencies for characters in the string and calculate standard deviation
     * from english language frequencies.
     *
     * @param s String to analyze.
     * @return
     */
    public static double frequencyDistance(String s) {
        final Map<Character, Double> charFrequencies = charFrequencies(s);
        return ENGLISH_CHAR_FREQUENCY.entrySet().stream()
                .map(e -> frequencyDiff(e.getKey(), charFrequencies.getOrDefault(e.getKey(), 0.0)))
                .reduce(0.0, Double::sum);
    }

    private static double frequencyDiff(char c, double freq) {
        double englishFreq = ENGLISH_CHAR_FREQUENCY.getOrDefault(c, 0.0);
        return Math.abs(englishFreq - freq);
    }

    /**
     * Calculates relative character frequencies in percentages
     *
     * @param s text to be analysed
     * @return a Map where key is a character from s and value is its relative frequency percentage
     */
    public static Map<Character, Double> charFrequencies(String s) {
        Map<Character, Double> result = new HashMap<>();
        charHistogram(s).forEach((k, v) -> result.put(k, v * 100.0 / (double) s.length()));
        return result;
    }

    public static Map<Character, Long> charHistogram(String s) {
        return s.codePoints()
                .boxed()
                .map(Character::toLowerCase)
                .collect(groupingBy(i -> (char) i.intValue(), counting()));
    }

    /**
     * Count number of occurrences of a character in a string. Ignores case.
     *
     * @param s String to be analyzed
     * @param c character to count
     * @return count of c in s
     */
    public static long countChars(String s, char c) {
        return s.toLowerCase()
                .codePoints()
                .filter(cp -> (char) cp == Character.toLowerCase(c))
                .count();
    }
}
