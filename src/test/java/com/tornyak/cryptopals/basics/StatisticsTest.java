package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    private static final String TEXT = "Etaoin shrdlu is a nonsense phrase that sometimes appeared in print" +
            "in the days of \"hot type\" publishing because of a custom of type-casting" +
            "machine operators to fill out and discard lines of type when an error was made." +
            " It appeared often enough to become part of newspaper lore, and \"etaoin shrdlu\" is" +
            "listed in the Oxford English Dictionary and in the Random House Webster's Unabridged Dictionary." +
            "It is the approximate order of frequency of the 12 most commonly used letters in the English language.";

    @Test
    void countChars() {
        assertEquals(0, Statistics.countChars("", 'a'));
        assertEquals(1, Statistics.countChars("a", 'a'));
        assertEquals(1, Statistics.countChars("A", 'a'));
        assertEquals(1, Statistics.countChars("abc", 'a'));
        assertEquals(2, Statistics.countChars("abAc", 'a'));
        assertEquals(2, Statistics.countChars("abaBc", 'B'));
        assertEquals(1, Statistics.countChars("ababc", 'c'));
        assertEquals(3, Statistics.countChars("ababBc", 'b'));
    }
    @Test
    void charHistogram() {
        Map<Character, Long> histogram = Statistics.charHistogram("");
        assertEquals(0, histogram.size());
        histogram = Statistics.charHistogram("abc");

        assertEquals(3, histogram.size());
        assertEquals(1, histogram.get('a'));
        assertEquals(1, histogram.get('b'));
        assertEquals(1, histogram.get('c'));

        histogram = Statistics.charHistogram("abcbcc");
        assertEquals(3, histogram.size());
        assertEquals(1, histogram.get('a'));
        assertEquals(2, histogram.get('b'));
        assertEquals(3, histogram.get('c'));
    }

    @Test
    void charFrequencies() {
        Map<Character, Double> freq = Statistics.charFrequencies("aaaaaaaaaa");
        assertEquals(100., freq.get('a'), 0.000001);

        freq = Statistics.charFrequencies("aaaabbbb");
        assertEquals(50., freq.get('a'), 0.000001);
        assertEquals(50., freq.get('b'), 0.000001);

        freq = Statistics.charFrequencies("abababab");
        assertEquals(50., freq.get('a'), 0.000001);
        assertEquals(50., freq.get('b'), 0.000001);

        freq = Statistics.charFrequencies("ababccccabab");
        assertEquals(100./3, freq.get('a'), 0.000001);
        assertEquals(100./3, freq.get('b'), 0.000001);
        assertEquals(100./3, freq.get('c'), 0.000001);
    }

    @Test
    void textFrequencies() {
        final var freq = Statistics.charFrequencies(TEXT);
        final var expectedChars = " etnisrdhplumcybw.x2";

        for (char c : expectedChars.toCharArray()) {
            double frequency = freq.get(c);
            double expectedFrequency = 100. * Statistics.countChars(TEXT, c) / TEXT.length();

            assertEquals(expectedFrequency, frequency, "Wrong frequency for character: " + c);
        }
    }

    @Test
    void frequencyDistance() {
        String text100chars = "aaaaaaaabccddddeeeeeeeeeeeeeffgghhhhhhiiiiiiikllllmmnnnnnnnooooooopprrrrrrssssssttttttttttuuuvwwwyyz";
        assertEquals(100.064, Statistics.frequencyDistance(text100chars), 0.00001);
        assertEquals(100.37163, Statistics.frequencyDistance(TEXT), 0.00001);
    }

    @ParameterizedTest
    @MethodSource("hammingDistanceArgs")
    void hammingDistance(int result, String s1, String s2) {
        assertEquals(result, Statistics.hummingDistance(s1, s2));
    }

    private static Stream<Arguments> hammingDistanceArgs() {
        return Stream.of(
            Arguments.of(0, "a", "a"),
            Arguments.of(2, "a", "b"),
            Arguments.of(37, "this is a test", "wokka wokka!!!")
        );
    }
}