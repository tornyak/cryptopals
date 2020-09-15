package com.tornyak.cryptopals.rand;

import com.tornyak.cryptopals.basics.Bytes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class MtCypherTest {

    @Test
    void encryptDecrypt() {
        byte[] data = "Hello world!".getBytes();
        byte[] key = Bytes.random(16);
        assertArrayEquals(data, MtCypher.decrypt(MtCypher.encrypt(data, key), key));
    }


    @Test
    @DisplayName("Set 3 challenge 24 - Create the MT19937 stream cipher and break it")
    void breakStreamCipher() {
        byte[] plaintext = generatePlaintext();
        byte[] key = Bytes.random(2);

        byte[] ciphertext = MtCypher.encrypt(plaintext, key);
        boolean guessed = false;

        // guess the key
        for (int i = 0; i < 1 << 16; i++) {
            String decrypted = new String(MtCypher.decrypt(ciphertext, Bytes.fromInteger(i)));
            if (decrypted.endsWith("A".repeat(14))) {
                assertEquals(Bytes.toInteger(key), i);
                guessed = true;
            }
        }
        assertTrue(guessed);
    }

    private byte[] generatePlaintext() {
        String chars = "abcdefghijklmnopqrstuvwxwz";
        StringBuilder result = new StringBuilder();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < rnd.nextInt(10, 4096); i++) {
            char c = chars.charAt(rnd.nextInt(chars.length()));
            result.append(c);
        }
        result.append("A".repeat(14));
        return result.toString().getBytes();
    }

    @Test
    void passwordResetToken() {
        long startTime = System.currentTimeMillis();
        byte[] token = generatePasswordResetToken();
        long stopTime = System.currentTimeMillis();
        boolean broken = false;

        start:
        for (int i = 0; i < stopTime - startTime; i++) {
            int guess = (int) (startTime + i);
            MersenneTwisterRng rng = new MersenneTwisterRng(guess);
            for (int j = 0; j < 4; j++) {
                byte[] bytes = Bytes.fromInteger((int) rng.nextInt32());
                for (int k = 0; k < 4; k++) {
                    if (bytes[k] != token[j * 4 + k]) {
                        continue start;
                    }
                }
            }
            broken = true;
            break;
        }
        assertTrue(broken);
    }

    private byte[] generatePasswordResetToken() {
        byte[] result = new byte[0]; // assume 16 bytes token even if not in the assignment
        int seed = (int) System.currentTimeMillis();
        MersenneTwisterRng rng = new MersenneTwisterRng(seed);
        for (int i = 0; i < 4; i++) {
            result = Bytes.append(result, Bytes.fromInteger((int) rng.nextInt32()));
        }
        return result;
    }
}