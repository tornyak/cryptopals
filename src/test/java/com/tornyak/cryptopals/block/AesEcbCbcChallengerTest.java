package com.tornyak.cryptopals.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AesEcbCbcChallengerTest {

    @Test
    @DisplayName("Set 2 challenge 11 - An ECB/CBC detection oracle")
    void encryptionOracle() {
        AesEcbCbcChallenger challenger = new AesEcbCbcChallenger();
        byte[] input = new byte[48];
        List<AesEcbCbcChallenger.Mode> guesses = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            byte[] challenge = challenger.encryptionOracle(input);
            guesses.add(guessMode(challenge));
        }
        assertTrue(challenger.isGuessCorrect(guesses));
    }

    private AesEcbCbcChallenger.Mode guessMode(byte[] data) {
        if (secondAndThirdBlocksEqual(data)) {
            return AesEcbCbcChallenger.Mode.ECB;
        }
        return AesEcbCbcChallenger.Mode.CBC;
    }

    private boolean secondAndThirdBlocksEqual(byte[] data) {
        int blockSize = 16;
        return Arrays.compare(data, blockSize, 2 * blockSize, data, 2 * blockSize, 3 * blockSize) == 0;
    }
}