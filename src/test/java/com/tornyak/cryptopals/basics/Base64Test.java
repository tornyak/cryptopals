package com.tornyak.cryptopals.basics;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base64Test {

    @ParameterizedTest
    @MethodSource("encodeArguments")
    public void encode(String input, String expected) {
        assertArrayEquals(expected.getBytes(), Base64.encode(input.getBytes()));
    }

    static Stream<Arguments> encodeArguments() {
        return Stream.of(
                Arguments.of("",""),
                Arguments.of("€","4oKs"),
                Arguments.of("a","YQ=="),
                Arguments.of("ab","YWI="),
                Arguments.of("abc","YWJj"),
                Arguments.of("abcd","YWJjZA=="),
                Arguments.of("abcde","YWJjZGU="),
                Arguments.of("abcdef","YWJjZGVm"),
                Arguments.of("abcdefg","YWJjZGVmZw==")
        );
    }

    @ParameterizedTest
    @MethodSource("calculateSizeArguments")
    public void calculateSize(int input, int expected) {
        assertEquals(expected, Base64.calculateSize(input));
    }

    private static Stream<Arguments> calculateSizeArguments() {
        return Stream.of(
                Arguments.of(0,0),
                Arguments.of(1,4),
                Arguments.of(2,4),
                Arguments.of(3,4),
                Arguments.of(4,8),
                Arguments.of(6,8),
                Arguments.of(7,12)
        );
    }

    @Test
    public void encodeThreeBytes() {
        for(int i = 0; i < 255; i++) {
            for(int j = 0; j < 255; j++) {
                for(int k = 0; k < 255; k++) {
                    final var src = new byte[]{(byte)i, (byte)j, (byte)k};
                    var dst = new byte[4];
                    Base64.encodeThreeBytes(src, 0, dst, 0);
                    assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
                }
            }
        }
    }

    @Test
    public void encodeTwoBytes() {
        for(int i = 0; i < 255; i++) {
            for(int j = 0; j < 255; j++) {
                final var src = new byte[]{(byte)i, (byte)j};
                var dst = new byte[4];
                Base64.encodeTwoBytes(src, 0, dst, 0);
                assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
            }
        }
    }

    @Test
    public void encodeOneByte() {
        for(int i = 0; i < 255; i++) {
            final var src = new byte[]{(byte)i};
            var dst = new byte[4];
            Base64.encodeOneByte(src, 0, dst, 0);
            assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
        }
    }
}