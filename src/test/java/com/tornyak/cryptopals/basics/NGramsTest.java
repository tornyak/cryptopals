package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NGramsTest {

    private static NGrams nGrams;

    @BeforeAll
    static void setUp() throws Exception {
        Path[] paths = List.of("ngrams1.txt", "ngrams2.txt", "ngrams3.txt")
                .stream()
                .map(NGramsTest.class.getClassLoader()::getResource)
                .filter(Objects::nonNull)
                .map(NGramsTest::urlToUri)
                .map(Path::of)
                .toArray(Path[]::new);

        nGrams = NGrams.loadFromFiles(paths);
    }

    private static URI urlToUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException();
        }
    }

    @ParameterizedTest
    @MethodSource("getFrequencyArguments")
    void getFrequency(String letter, double frequency) throws Exception {
        assertEquals(frequency, nGrams.getFrequency(letter), 0.0001);
    }

    static Stream<Arguments> getFrequencyArguments() {
        return Stream.of(
                Arguments.of("E", 0.1249),
                Arguments.of("e", 0.1249),
                Arguments.of("", 0.0),
                Arguments.of(" ", 0.0),
                Arguments.of("Å", 0.0),
                Arguments.of("th", 0.0356),
                Arguments.of("ur", 0.0054),
                Arguments.of("z ", 0.0),
                Arguments.of("  ", 0.0),
                Arguments.of("the", 0.0329),
                Arguments.of("rat", 0.00206)
        );
    }
}