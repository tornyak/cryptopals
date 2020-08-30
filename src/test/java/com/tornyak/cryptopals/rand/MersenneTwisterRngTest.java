package com.tornyak.cryptopals.rand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("untemperArguments")
    void untemper(int x) {
        assertEquals(x, MersenneTwisterRng.untemper(MersenneTwisterRng.temper(x)));
    }

    static Stream<Arguments> untemperArguments() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(0xf),
                Arguments.of(0xff),
                Arguments.of(0xfff),
                Arguments.of(0xffff),
                Arguments.of(0xfffff),
                Arguments.of(0xffffff),
                Arguments.of(0xfffffff),
                Arguments.of(0xffffffff),
                Arguments.of(0xa),
                Arguments.of(0xba),
                Arguments.of(0xcba),
                Arguments.of(0xdcba),
                Arguments.of(0xedcba),
                Arguments.of(0xfedcba),
                Arguments.of(0xbfedcba),
                Arguments.of(0xabfedcba),
                Arguments.of(0xaaaaaaaa),
                Arguments.of(0xfedcba98),
                Arguments.of(0xfedcfedc)
        );
    }

    @Test
    @DisplayName("Set 3 Challenge 23 - Clone an MT19937 RNG from its output")
    void cloneMtRngFromItsOuptut() {
        int[] state = new int[624];
        MersenneTwisterRng mtRng = new MersenneTwisterRng(0);
        for (int i = 0; i < state.length; i++) {
            state[i] = MersenneTwisterRng.untemper((int)mtRng.nextInt32());
        }

        MersenneTwisterRng mtRngClone = new MersenneTwisterRng(state);
        for (int i = 0; i < 1000; i++) {
            assertEquals(mtRng.nextInt32(), mtRngClone.nextInt32());
        }
    }
}
