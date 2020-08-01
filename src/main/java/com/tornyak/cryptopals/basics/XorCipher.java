package com.tornyak.cryptopals.basics;

import java.util.Arrays;

public class XorCipher {

    private XorCipher() {
    }

    public static String encrypt(String hexPlaintext, byte key) {
        final byte[] plaintextBytes = Hex.stringToBytes(hexPlaintext);
        final byte[] ciphertext = xor(plaintextBytes, key);
        return Hex.stringFromBytes(ciphertext);
    }

    public static String encrypt(String plaintext, String key) {
        final byte[] plaintextBytes = plaintext.getBytes();
        final byte[] keyBytes = key.getBytes();
        final byte[] ciphertext = new byte[plaintextBytes.length];

        for (int i = 0, j = 0; i < ciphertext.length; i++, j++) {
            if (j >= keyBytes.length) {
                j = 0;
            }
            ciphertext[i] = (byte) (plaintextBytes[i] ^ keyBytes[j]);
        }
        return Hex.stringFromBytes(ciphertext);
    }

    public static String decrypt(byte[] ciphertext, String key) {
        final byte[] keyBytes = key.getBytes();
        final byte[] plaintextBytes = new byte[ciphertext.length];

        for (int i = 0, j = 0; i < ciphertext.length; i++, j++) {
            if (j >= keyBytes.length) {
                j = 0;
            }
            plaintextBytes[i] = (byte) (ciphertext[i] ^ keyBytes[j]);
        }
        return new String(plaintextBytes);
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        final byte[] plaintextBytes = new byte[ciphertext.length];
        for (int i = 0, j = 0; i < ciphertext.length; i++, j++) {
            if (j >= key.length) {
                j = 0;
            }
            plaintextBytes[i] = (byte) (ciphertext[i] ^ key[j]);
        }
        return plaintextBytes;
    }

    public static String decrypt(String ciphertext, byte key) {
        return encrypt(ciphertext, key);
    }

    public static byte[] decrypt(byte[] ciphertext, byte key) {
        return xor(ciphertext, key);
    }

    private static byte[] xor(byte[] bytes, byte key) {
        final byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte)(bytes[i] ^ key);
        }
        return result;
    }

    public static byte[] decryptTillCommonLength(byte[] ciphertext, byte[] key) {
        int length = Math.min(ciphertext.length, key.length);
        byte[] c = (ciphertext.length > length) ? Arrays.copyOf(ciphertext, length) : ciphertext;
        byte[] k = (key.length > length) ? Arrays.copyOf(key, length) : key;
        return decrypt(c, k);
    }
}
