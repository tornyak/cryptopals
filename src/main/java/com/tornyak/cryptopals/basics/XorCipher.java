package com.tornyak.cryptopals.basics;

public class XorCipher {

    private XorCipher() {
    }

    public static String encrypt(String plaintext, byte key) {
        String expandedKey = buildKey(key, plaintext.length() / 2);
        return Hex.xor(plaintext, expandedKey);
    }

    public static String decrypt(String ciphertext, byte key) {
        return encrypt(ciphertext, key);
    }

    private static String buildKey(byte c, int length) {
        String hexChar = Hex.stringFromBytes(new byte[]{c});
        return hexChar.repeat(Math.max(0, length));
    }
}
