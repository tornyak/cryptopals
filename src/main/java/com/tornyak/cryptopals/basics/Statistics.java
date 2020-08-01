package com.tornyak.cryptopals.basics;

import org.apache.commons.lang3.CharUtils;

import java.util.*;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

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

    private Statistics() {
    }

    /**
     * Method will calculate frequencies for characters in the string and calculate standard deviation
     * from english language frequencies.
     *
     * @param s String to analyze.
     * @return
     */
    public static double frequencyDistance(String s) {
        final Map<Character, Double> charFreq = charFrequencies(s);
        ENGLISH_CHAR_FREQUENCY.keySet()
                .stream()
                .filter(not(charFreq::containsKey))
                .forEach(k -> charFreq.put(k, 0.0));

        return charFreq.entrySet().stream()
                .map(e -> frequencyDiff(e.getKey(), e.getValue()))
                .reduce(0.0, Double::sum);
    }

    private static double frequencyDiff(char c, double freq) {
        if (Character.isSpaceChar(c)) {
            return 0;
        }
        if (!CharUtils.isAsciiPrintable(c)) {
            return 1000 * freq;
        }
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

    public static double sumOfLetterFrequencySquares(String s) {
        String alphaString = s.chars()
                .filter(Character::isAlphabetic)
                .mapToObj(Character::toString)
                .collect(joining());

        return charHistogram(alphaString)
                .values()
                .stream()
                .mapToDouble(c -> Math.pow((double) c / s.length(), 2))
                .sum();
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

    public static int hummingDistance(String s1, String s2) {
        Objects.requireNonNull(s1, s2);
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("length of s1 and s2 must be equal");
        }

        final char[] chars1 = s1.toCharArray();
        final char[] chars2 = s2.toCharArray();
        int result = 0;

        for (int i = 0; i < chars1.length; i++) {
            result += Integer.bitCount((chars1[i] ^ chars2[i]) & 0xFFFF);
        }
        return result;
    }
}
