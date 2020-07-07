package com.tronyak.cryptopals.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.BadPaddingException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PadTest {

    @ParameterizedTest
    @MethodSource("pkcs7Arguments")
    @DisplayName("Set 2 challenge 9 - Implement PKCS#7 padding")
    void pkcs7(int blockSize, byte[] data, byte[] pad) {
        assertArrayEquals(padData(data, pad), Pad.pkcs7(data, blockSize));
    }

    @ParameterizedTest
    @MethodSource("pkcs7RemovePaddingArguments")
    @DisplayName("Set 2 challenge 15 - PKCS#7 padding validation")
    void pkcs7RemovePadding(byte[] dataWithoutPad, byte[] dataWithPad) throws BadPaddingException {
        assertArrayEquals(dataWithoutPad, Pad.removePkcs7(dataWithPad));
    }

    @ParameterizedTest
    @MethodSource("pkcs7IllegalPaddingArguments")
    void pkcs7IllegalPaddingbyte(byte[] data) {
        assertThrows(BadPaddingException.class, () -> Pad.removePkcs7(data));
    }

    @Test
    void pkcs7IllegalBlockSize() {
        assertThrows(IllegalArgumentException.class, () -> Pad.pkcs7("data".getBytes(), -1));
        assertThrows(IllegalArgumentException.class, () -> Pad.pkcs7("data".getBytes(), 256));
    }

    @ParameterizedTest
    @MethodSource("paddingZerosArguments")
    void countPaddingZeros(int zeroCnt, byte[] data) {
        assertEquals(zeroCnt, Pad.countPaddingZeros(data));
    }

    static Stream<Arguments> paddingZerosArguments() {
        return Stream.of(
                Arguments.of(0, new byte[0]),
                Arguments.of(0, new byte[]{1}),
                Arguments.of(0, new byte[]{1, 2}),
                Arguments.of(0, new byte[]{1, 2, 3}),
                Arguments.of(1, new byte[]{1, 2, 3, 0}),
                Arguments.of(2, new byte[]{1, 2, 3, 0, 0}),
                Arguments.of(3, new byte[]{1, 2, 3, 0, 0, 0}),
                Arguments.of(1, new byte[]{0}),
                Arguments.of(2, new byte[]{0, 0}),
                Arguments.of(3, new byte[]{0, 0, 0}),
                Arguments.of(0, new byte[]{0 ,0, 0, 1}),
                Arguments.of(1, new byte[]{0 ,0, 0, 1, 0})
        );
    }

    static Stream<Arguments> pkcs7RemovePaddingArguments() {
        return Stream.of(
                Arguments.of("ICE ICE BABY".getBytes(), "ICE ICE BABY\u0004\u0004\u0004\u0004".getBytes())
        );
    }

    static Stream<Arguments> pkcs7IllegalPaddingArguments() {
        return Stream.of(
                Arguments.of("ICE ICE BABY\u0005\u0005\u0005\u0005".getBytes()),
                Arguments.of("ICE ICE BABY\u0001\u0002\u0003\u0004".getBytes())
        );
    }

    private byte[] padData(byte[] data, byte[] pad) {
        byte[] result = new byte[data.length + pad.length];
        System.arraycopy(data, 0, result, 0, data.length);
        System.arraycopy(pad, 0, result, data.length, pad.length);
        return result;
    }

    static Stream<Arguments> pkcs7Arguments() {
        return Stream.of(
                Arguments.of(1, "".getBytes(), getPadBytes(1)),
                Arguments.of(1, "X".getBytes(), getPadBytes(1)),
                Arguments.of(16, "YELLOW SUBMARINE".getBytes(), getPadBytes(16)),
                Arguments.of(20, "YELLOW SUBMARINE".getBytes(), getPadBytes(4))
        );
    }

    static byte[] getPadBytes(int n) {
        byte[] result = new byte[n];
        Arrays.fill(result, (byte)n);
        return result;
    }
}