package com.tronyak.cryptopals.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @Test
    void pkcs7IllegalBlockSize() {
        assertThrows(IllegalArgumentException.class, () -> Pad.pkcs7("data".getBytes(), -1));
        assertThrows(IllegalArgumentException.class, () -> Pad.pkcs7("data".getBytes(), 256));
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