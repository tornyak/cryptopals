package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AesCtrTest {

    private static int blockSize = 16;

    @Test
    @DisplayName("Set 3 challenge 18 - Implement CTR")
    void decryptText() {
        byte[] key = "YELLOW SUBMARINE".getBytes();
        byte[] iv = Bytes.filledWithValue(blockSize, (byte)0);
        byte[] ciphertext = Base64.getDecoder().decode("L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ==");

        final byte[] plaintext = AesCtr.decrypt(ciphertext, key, iv);

        assertEquals("Yo, VIP Let's kick it Ice, Ice, baby Ice, Ice, baby ", new String(plaintext));
    }

    @ParameterizedTest
    @MethodSource("ctrArguments")
    void incrementCounter(byte[] ctr, byte[] incCtr) {
        assertArrayEquals(incCtr, AesCtr.increment(ctr));
    }

    static Stream<Arguments> ctrArguments() {
        return Stream.of(
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF, (byte)0xFF, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFE},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})
        );
    }
}