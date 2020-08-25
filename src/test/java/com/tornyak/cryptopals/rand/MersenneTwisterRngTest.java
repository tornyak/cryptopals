package com.tornyak.cryptopals.rand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MersenneTwisterRngTest {

    private int seed = 0;

    @Test
    @DisplayName("Set 3 Challenge 21 - Implement the MT19937 Mersenne Twister RNG")
    void int32SequenceMatchesCImplementation() {
        MersenneTwisterRng mt = new MersenneTwisterRng(0);
        assertEquals(2357136044L, mt.nextInt32());
        assertEquals(2546248239L, mt.nextInt32());
    }

    @Test
    void sameSeedGivesSameRandomSequence() {
        int seed = (int) System.currentTimeMillis();
        MersenneTwisterRng mt1 = new MersenneTwisterRng(seed);
        MersenneTwisterRng mt2 = new MersenneTwisterRng(seed);
        for (int i = 0; i < 1000; i++) {
            assertEquals(mt1.nextInt32(), mt2.nextInt32());
        }
    }

    @Test
    @DisplayName("Set 3 Challenge 22 - Crack an MT19937 seed")
    void crackAnMt19927Seed() throws InterruptedException {
        int start = (int) System.currentTimeMillis();
        long n = generateMtRandomNumber();
        int stop = (int) System.currentTimeMillis();

        boolean found = false;

        for (int i = start; i <= stop; i++) {
            MersenneTwisterRng mt = new MersenneTwisterRng(i);
            if (mt.nextInt32() == n) {
                found = true;
                assertEquals(seed, i);
                break;
            }
        }

        assertTrue(found);
    }

    private long generateMtRandomNumber() throws InterruptedException {
        sleepRandom();

        seed = (int) System.currentTimeMillis();
        System.out.println("Seed=" + seed);
        MersenneTwisterRng mtRng = new MersenneTwisterRng(seed);

        sleepRandom();

        return mtRng.nextInt32();
    }

    private void sleepRandom() throws InterruptedException {
        Random rng = new Random();
        Duration duration = Duration.ofSeconds(40 + rng.nextInt(960));
        System.out.println("Sleeping: " + duration);
        Thread.sleep(duration.toMillis());
    }
}
