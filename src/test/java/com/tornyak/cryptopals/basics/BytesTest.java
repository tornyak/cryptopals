package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BytesTest {

    @ParameterizedTest
    @MethodSource("splitIntoBlocksSource")
    void splitIntoBlocks(byte[] original, byte[][] expected, int numOfBlocks) {
        byte[][] split = Bytes.splitIntoBlocks(original, numOfBlocks);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], split[i]);
        }
    }

    static Stream<Arguments> splitIntoBlocksSource() {
        return Stream.of(
                Arguments.of(new byte[]{0,1,2,3,4,5,6,7,8}, new byte[][]{{0,1,2}, {3,4,5}, {6,7,8}}, 3),
                Arguments.of(new byte[]{0,1,2,3,4,5,6,7,8,9}, new byte[][]{{0,1,2},{3,4,5},{6,7,8}, {9,0,0}}, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("appendArraySource")
    void appendArray(byte[] destination, byte[] source, byte[] expected) throws Exception {
        assertArrayEquals(expected, Bytes.append(destination, source));
    }

    static Stream<Arguments> appendArraySource() {
        return Stream.of(
                Arguments.of(new byte[0], new byte[0], new byte[0]),
                Arguments.of(new byte[0], "Hello".getBytes(), "Hello".getBytes()),
                Arguments.of("Hello".getBytes(), "World".getBytes(), "HelloWorld".getBytes())
        );
    }


    @Test
    void isAsciiPrintable() throws Exception {
        assertTrue(Bytes.isAsciiPrintable("Now that the party is jumping\n".getBytes()));
        assertFalse(Bytes.isAsciiPrintable(new byte[]{11,32}));
    }

    @Test
    void getLetterPercentage() throws Exception {
        assertEquals(100, Bytes.getAsciiLetterPercentage("Hello".getBytes()), 0.00001);
        assertEquals(0, Bytes.getAsciiLetterPercentage("12345".getBytes()), 0.00001);
        assertEquals(50, Bytes.getAsciiLetterPercentage("Hello12345".getBytes()), 0.00001);
        assertEquals(83.33333, Bytes.getAsciiLetterPercentage("Hello World!".getBytes()), 0.00001);
    }
}