package com.tornyak.cryptopals.basics;

public class XorCipher {

    private XorCipher() {
    }

    public static String encrypt(String hexPlaintext, byte key) {
        final byte[] plaintextBytes = Hex.stringToBytes(hexPlaintext);
        final byte[] ciphertext = new byte[plaintextBytes.length];
        for (int i = 0; i < ciphertext.length; i++) {
            ciphertext[i] = (byte)(plaintextBytes[i] ^ key);
        }
        return Hex.stringFromBytes(ciphertext);
    }

    public static String decrypt(byte[] ciphertext, String key) {
        final byte[] keyBytes = key.getBytes();
        final byte[] plaintextBytes = new byte[ciphertext.length];

        for (int i = 0, j = 0; i < ciphertext.length; i++, j++) {
            if(j >= keyBytes.length) {
                j = 0;
            }
            plaintextBytes[i] = (byte)(ciphertext[i] ^ keyBytes[j]);
        }
        return new String(plaintextBytes);
    }

    public static String encrypt(String plaintext, String key) {
        final byte[] plaintextBytes = plaintext.getBytes();
        final byte[] keyBytes = key.getBytes();
        final byte[] ciphertext = new byte[plaintextBytes.length];

        for (int i = 0, j = 0; i < ciphertext.length; i++, j++) {
            if(j >= keyBytes.length) {
                j = 0;
            }
            ciphertext[i] = (byte)(plaintextBytes[i] ^ keyBytes[j]);
        }
        return Hex.stringFromBytes(ciphertext);
    }

    public static String decrypt(String ciphertext, byte key) {
        return encrypt(ciphertext, key);
    }

    private static String extendKey(byte c, int length) {
        String hexChar = Hex.stringFromBytes(new byte[]{c});
        return hexChar.repeat(Math.max(0, length));
    }

    private static String extendKey(String key, int length) {
        String hexKey = Hex.stringFromBytes(key.getBytes());
        String extendedKey =  hexKey.repeat((length/hexKey.length()));
        int lengthDiff = length - extendedKey.length();
        return extendedKey + hexKey.substring(0, lengthDiff);
    }
}
