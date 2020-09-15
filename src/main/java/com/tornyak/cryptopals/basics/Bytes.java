package com.tornyak.cryptopals.basics;

import org.apache.commons.lang3.CharUtils;

import java.security.SecureRandom;
import java.util.Arrays;

public final class Bytes {

    private static final SecureRandom RNG = new SecureRandom();

    private Bytes() {
    }

    public static byte[][] splitIntoBlocks(byte[] bytes, int size) {
        int blocks = (bytes.length + size - 1) / size;
        byte[][] result = new byte[blocks][size];
        int remaining = bytes.length;
        for (int i = 0; (i < blocks - 1) && (remaining >= size); i++) {
            result[i] = Arrays.copyOfRange(bytes, size * i, size * (i + 1));
            remaining -= size;
        }
        if (remaining > 0) {
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
        byte mask = (byte) (1 << i);
        return (byte) (b ^ mask);
    }

    public static byte[] filledWithValue(int size, byte value) {
        final byte[] result = new byte[size];
        Arrays.fill(result, value);
        return result;
    }

    public static byte[] append(byte[] bytes, byte b) {
        final byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        result[bytes.length] = b;
        return result;
    }

    public static byte[] append(byte[] b1, byte[] b2) {
        final byte[] result = new byte[b1.length + b2.length];
        if (b1.length > 0) {
            System.arraycopy(b1, 0, result, 0, b1.length);
        }
        if (b2.length > 0) {
            System.arraycopy(b2, 0, result, b1.length, b2.length);
        }
        return result;
    }

    public static boolean isAsciiPrintable(byte[] s) {
        for (byte b : s) {
            char c = (char) b;
            if (!CharUtils.isAsciiPrintable(c) && c != CharUtils.CR && c != CharUtils.LF) {
                return false;
            }
        }
        return true;
    }

    public static double getAsciiLetterPercentage(byte[] bytes) {
        int cnt = 0;
        for (byte aByte : bytes) {
            if (CharUtils.isAsciiAlpha((char) aByte)) {
                cnt++;
            }
        }
        return 100.0 * cnt / bytes.length;
    }

    public static int toInteger(byte[] bytes) {
        if (bytes.length > Integer.SIZE) {
            throw new IllegalArgumentException(" bytes length: " + bytes.length + " > " + Integer.SIZE);
        }
        int result = 0;
        for (byte aByte : bytes) {
            result <<= 8;
            result += aByte & 0xFF;
        }
        return result;
    }

    public static byte[] fromInteger(int i) {
        byte[] result = new byte[Integer.SIZE / 8];
        for (int j = 0; j < result.length; j++) {
            result[j] = (byte) (i >>> (8 * (3 - j)));
        }
        return result;
    }
}
