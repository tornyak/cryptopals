package com.tornyak.cryptopals.block;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class AesEcbCbcChallenger {
    private static final SecureRandom rng = new SecureRandom();

    public enum Mode {
        ECB, CBC
    }

    private List<Mode> choices = new ArrayList<>();

    public byte[] encryptionOracle(byte[] data) {
        byte[] paddedData = randomPadData(data);
        byte[] key = generateKey(16);
        byte[] iv = generateKey(16);

        int aesMode = rng.nextInt(2);

        if (aesMode == 0) {
            choices.add(Mode.ECB);
            return AesEcb.encrypt(paddedData, key);
        }
        choices.add(Mode.CBC);
        return AesCbc.encrypt(paddedData, key, iv);
    }

    private byte[] randomPadData(byte[] data) {
        byte[] prependBytes = new byte[5 + rng.nextInt(6)];
        byte[] appendBytes = new byte[5 + rng.nextInt(6)];

        rng.nextBytes(prependBytes);
        rng.nextBytes(appendBytes);

        byte[] result = new byte[prependBytes.length + data.length + appendBytes.length];
        System.arraycopy(prependBytes, 0, result, 0, prependBytes.length);
        System.arraycopy(data, 0, result, prependBytes.length, data.length);
        System.arraycopy(appendBytes, 0, result, prependBytes.length + data.length, appendBytes.length);

        return result;
    }

    private byte[] generateKey(int keySize) {
        byte[] key = new byte[keySize];
        rng.nextBytes(key);
        return key;
    }

    public boolean isGuessCorrect(List<Mode> guesses) {
        return guesses.equals(choices);
    }
}
