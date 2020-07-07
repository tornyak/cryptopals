package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CbcPaddingOracle {

    private static List<String> ORACLE_STRINGS = List.of(
            "MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc=",
            "MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic=",
            "MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw==",
            "MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg==",
            "MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl",
            "MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA==",
            "MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw==",
            "MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8=",
            "MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g=",
            "MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93");

    private static final int blockSize = 16;
    private final byte[] key = Bytes.random(blockSize);
    private final byte[] iv = Bytes.random(blockSize);
    private final byte[] ciphertext;

    public CbcPaddingOracle() {
        String s = selectRandomString(ORACLE_STRINGS);
        ciphertext = AesCbc.encrypt(s.getBytes(), key, iv);
    }

    /**
     * @return IV || ciphertext
     */
    public byte[] getCiphertextWithIv() {
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
        return result;
    }

    byte[] decrypt(byte[] ciphertextWithIv) {
        byte[] ivx = Arrays.copyOf(ciphertextWithIv, blockSize);
        byte[] c = Arrays.copyOfRange(ciphertextWithIv, blockSize, ciphertextWithIv.length);
        return AesCbc.decrypt(c, key, ivx);
    }


    private String selectRandomString(List<String> strings) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int i = random.nextInt(0, strings.size());
        return strings.get(i);
    }
}
