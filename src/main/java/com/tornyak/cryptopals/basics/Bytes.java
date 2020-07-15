package com.tornyak.cryptopals.basics;

import java.security.SecureRandom;
import java.util.Arrays;

public final class Bytes {

    private static final SecureRandom RNG = new SecureRandom();

    public static byte[][] splitIntoBlocks(byte[] bytes, int size) {
        int blocks = (bytes.length + size - 1) / size;
        byte[][] result = new byte[blocks][size];
        int remaining = bytes.length;
        for (int i = 0; (i < blocks - 1) && (remaining >= size); i++) {
            result[i] = Arrays.copyOfRange(bytes, size * i, size * (i + 1));
            remaining-=size;
        }
        if(remaining > 0) {
            result[blocks - 1] = new byte[size];
            System.arraycopy(bytes, size * (blocks - 1), result[blocks - 1], 0, remaining);
        }
        return result;
    }

    public static byte[][] transpose(byte[][] b) {
        byte[][] result = new byte[b[0].length][b.length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                result[j][i] = b[i][j];
            }
        }
        return result;
    }

    public static byte[] xor(byte[] a, int i, byte[] b, int j, int size) {
        byte[] result = new byte[size];
        for (int k = 0; k < size; k++) {
            result[k] = (byte) (a[i + k] ^ b[j + k]);
        }
        return result;
    }

    /**
     * @param size length of random byte array
     * @return byte array of random bytes
     */
    public static byte[] random(int size) {
        byte[] result = new byte[size];
        RNG.nextBytes(result);
        return result;
    }

    public static byte flipBit(int i, byte b) {
        byte mask = (byte)(1 << i);
        return (byte)(b ^ mask);
    }

    public static byte[] filledWithValue(int size, byte value) {
        final byte[] result = new byte[size];
        Arrays.fill(result, value);
        return result;
    }
}
