package com.tornyak.cryptopals.rand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MersenneTwisterRngTest {

    @Test
    @DisplayName("Set 3 Challenge 21 - Implement the MT19937 Mersenne Twister RNG")
    void generateSequenceOfRandomNumbers() throws Exception {
        MersenneTwisterRng mt = new MersenneTwisterRng(0);
        assertEquals(2357136044L, mt.nextInt32());
        assertEquals(2546248239L, mt.nextInt32());
    }
}
