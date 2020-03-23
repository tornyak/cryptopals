package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XorCipherTest {

    @Test
    public void encrypt() {
        String hexMessage = Hex.stringFromBytes("This is the message!".getBytes());
        for(int k = 0; k < 65535; k++) {
            byte key = (byte)k;
            assertEquals(hexMessage, XorCipher.decrypt(XorCipher.encrypt(hexMessage, key), key));
        }
    }

    @Test
    void findKeyAndDecrypt() {
        final var ciphertext = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < 256; i++) {
            String hexPlaintext = XorCipher.decrypt(ciphertext, (byte)i);
            String plaintext = new String(Hex.stringToBytes(hexPlaintext));
            double distance = Statistics.frequencyDistance(plaintext);
            if(distance <= minDistance) {
                minDistance = distance;
                System.out.println(i + ": " + distance + " -> " + plaintext);
            }
        }
    }
}