package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                Arguments.of(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, new byte[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}}, 3),
                Arguments.of(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new byte[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {9, 0, 0}}, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("appendArraySource")
    void appendArray(byte[] destination, byte[] source, byte[] expected) {
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
    void isAsciiPrintable() {
        assertTrue(Bytes.isAsciiPrintable("Now that the party is jumping\n".getBytes()));
        assertFalse(Bytes.isAsciiPrintable(new byte[]{11, 32}));
    }

    @Test
    void getLetterPercentage() {
        assertEquals(100, Bytes.getAsciiLetterPercentage("Hello".getBytes()), 0.00001);
        assertEquals(0, Bytes.getAsciiLetterPercentage("12345".getBytes()), 0.00001);
        assertEquals(50, Bytes.getAsciiLetterPercentage("Hello12345".getBytes()), 0.00001);
        assertEquals(83.33333, Bytes.getAsciiLetterPercentage("Hello World!".getBytes()), 0.00001);
    }

    @ParameterizedTest
    @MethodSource("bytesToIntSource")
    void bytesToInt(byte[] bytes, int expected) {
        assertEquals(expected, Bytes.toInteger(bytes));
    }

    static Stream<Arguments> bytesToIntSource() {
        return Stream.of(
                Arguments.of(new byte[]{0}, 0),
                Arguments.of(new byte[]{1}, 1),
                Arguments.of(new byte[]{2}, 2),
                Arguments.of(new byte[]{0, 0, 1, 0}, 256),
                Arguments.of(new byte[]{0, 0, 1, 1}, 257)
        );
    }

    @ParameterizedTest
    @MethodSource("bytesFromIntSource")
    void bytesFromInt(byte[] expected, int n) {
        assertArrayEquals(expected, Bytes.fromInteger(n));
    }

    static Stream<Arguments> bytesFromIntSource() {
        return Stream.of(
                Arguments.of(new byte[]{0, 0, 0, 0}, 0),
                Arguments.of(new byte[]{0, 0, 0, 1}, 1),
                Arguments.of(new byte[]{0, 0, 0, 2}, 2),
                Arguments.of(new byte[]{0, 0, 1, 0}, 256),
                Arguments.of(new byte[]{0, 0, 1, 1}, 257)
        );
    }

    @Test
    void bytestFromAndToInt() {
        for (int i = 0; i < 8192; i++) {
            assertEquals(i, Bytes.toInteger(Bytes.fromInteger(i)));
        }
    }
}