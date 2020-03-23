package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    @Test
    void charFrequencies() {
        String text = "Etaoin shrdlu is a nonsense phrase that sometimes appeared in print" +
                        "in the days of \"hot type\" publishing because of a custom of type-casting" +
                        "machine operators to fill out and discard lines of type when an error was made." +
                        " It appeared often enough to become part of newspaper lore, and \"etaoin shrdlu\" is" +
                        "listed in the Oxford English Dictionary and in the Random House Webster's Unabridged Dictionary." +
                        "It is the approximate order of frequency of the 12 most commonly used letters in the English language.";

        final Map<Character, Double> freq = Statistics.charFrequencies(text);
        System.out.println(freq);
        TreeMap<Double, Character> sortedFreq = freq.entrySet()
                .stream()
                .collect(TreeMap::new, (m,e) -> m.put(e.getValue(), e.getKey()), TreeMap::putAll);
        while (!sortedFreq.isEmpty()){
            System.out.println(sortedFreq.pollLastEntry());
        }
    }

    @Test
    void simpleFrequencies() {
        Map<Character, Double> freq = Statistics.charFrequencies("aaaaaaaaaa");
        assertEquals(1.0, freq.get('a'), 0.000001);

        freq = Statistics.charFrequencies("aaaabbbb");
        assertEquals(0.5, freq.get('a'), 0.000001);
        assertEquals(0.5, freq.get('b'), 0.000001);

        freq = Statistics.charFrequencies("abababab");
        assertEquals(0.5, freq.get('a'), 0.000001);
        assertEquals(0.5, freq.get('b'), 0.000001);

        freq = Statistics.charFrequencies("ababccccabab");
        assertEquals(1./3, freq.get('a'), 0.000001);
        assertEquals(1./3, freq.get('b'), 0.000001);
        assertEquals(1./3, freq.get('c'), 0.000001);
    }

    @Test
    void frequencyDistance() {
        String text = "Etaoin shrdlu is a nonsense phrase that sometimes appeared in print" +
                "in the days of \"hot type\" publishing because of a custom of type-casting" +
                "machine operators to fill out and discard lines of type when an error was made." +
                " It appeared often enough to become part of newspaper lore, and \"etaoin shrdlu\" is" +
                "listed in the Oxford English Dictionary and in the Random House Webster's Unabridged Dictionary." +
                "It is the approximate order of frequency of the 12 most commonly used letters in the English language.";
        assertEquals(0.0593540, Statistics.frequencyDistance(text), 0.000001);
    }
}