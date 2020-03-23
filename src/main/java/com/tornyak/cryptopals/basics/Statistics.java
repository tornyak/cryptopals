package com.tornyak.cryptopals.basics;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Statistics {

    private static final Map<Character, Double> relativeFrequency = new HashMap<>();

    static {
        relativeFrequency.put('a', 8.167);
        relativeFrequency.put('b', 1.492);
        relativeFrequency.put('c', 2.202);
        relativeFrequency.put('d', 4.253);
        relativeFrequency.put('e', 12.702);
        relativeFrequency.put('f', 2.228);
        relativeFrequency.put('g', 2.015);
        relativeFrequency.put('h', 6.094);
        relativeFrequency.put('i', 6.966);
        relativeFrequency.put('j', 0.153);
        relativeFrequency.put('k', 1.292);
        relativeFrequency.put('l', 4.025);
        relativeFrequency.put('m', 2.406);
        relativeFrequency.put('n', 6.749);
        relativeFrequency.put('o', 7.507);
        relativeFrequency.put('p', 1.929);
        relativeFrequency.put('q', 0.095);
        relativeFrequency.put('r', 5.987);
        relativeFrequency.put('s', 6.327);
        relativeFrequency.put('t', 9.356);
        relativeFrequency.put('u', 2.758);
        relativeFrequency.put('v', 0.978);
        relativeFrequency.put('w', 2.560);
        relativeFrequency.put('x', 0.150);
        relativeFrequency.put('y', 1.994);
        relativeFrequency.put('z', 0.077);
    }

    /**
     * Method will calculate frequencies for characters in the string and calculate standard deviation
     * from english language frequencies.
     *
     * @param s string to
     * @return
     */
    public static double frequencyDistance(String s) {
        final Map<Character, Double> charFrequencies = charFrequencies(s);
        double result = relativeFrequency.entrySet().stream()
                .map(e -> absFrequencyDiffSquared(e.getKey(), charFrequencies.getOrDefault(e.getKey(), 0.0)))
                .reduce(0.0, Double::sum);
        return Math.sqrt(result);
    }

    private static double absFrequencyDiffSquared(char c, double freq) {
        double englishFreq = relativeFrequency.getOrDefault(c, 0.0);
        return Math.pow(Math.abs(englishFreq / 100 - freq), 2);
    }

    public static Map<Character, Double> charFrequencies(String s) {
        Map<Character, Double> result = new HashMap<>();
        charHistogram(s).forEach((k, v) -> result.put(k, (double) v / (double) s.length()));
        return result;
    }

    private static Map<Character, Long> charHistogram(String s) {
        return s.codePoints()
                .boxed()
                .map(Character::toLowerCase)
                .collect(groupingBy(i -> (char) i.intValue(), counting()));
    }

}
