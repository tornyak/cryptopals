package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BytesTest {

    @Test
    void splitIntoBlocks() {
        byte[] input = {0,1,2,3,4,5,6,7,8};
        byte[][] expected = {{0,1,2},{3,4,5},{6,7,8}};
        final byte[][] split = Bytes.splitIntoBlocks(input, 3);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], split[i]);
        }

        byte[] input2 = {0,1,2,3,4,5,6,7,8,9};
        byte[][] expected2 = {{0,1,2},{3,4,5},{6,7,8}, {9,0,0}};
        final byte[][] split2 = Bytes.splitIntoBlocks(input2, 3);
        for (int i = 0; i < expected2.length; i++) {
            assertArrayEquals(expected2[i], split2[i]);
        }
    }
}