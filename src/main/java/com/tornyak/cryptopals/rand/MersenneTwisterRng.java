package com.tornyak.cryptopals.rand;

public class MersenneTwisterRng {

    /* Period parameters */
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   /* constant vector a */
    private static final int UPPER_MASK = 0x80000000; /* most significant w-r bits */
    private static final int LOWER_MASK = 0x7fffffff; /* least significant r bits */

    private int[] mt = new int[N]; /* the array for the state vector  */
    private int mti;

    /* initializes mt[N] with a seed */
    public MersenneTwisterRng(int s) {
        mt[0] = s;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
        }
    }

    /* generates a random number on [0,0xffffffff]-interval */
    public long nextInt32() {
        int y;
        int[] mag01 = {0x0, MATRIX_A};
        /* mag01[x] = x * MATRIX_A  for x=0,1 */

        if (mti >= N) { /* generate N words at one time */
            int kk;

            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        y = temper(y);
        return ((long) y) & 0xFFFFFFFFL;
    }

    public static int temper(int y) {
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >>> 18);
        return y;
    }

    public static long untemper(int x) {
        return untemper1(untemper2(untemper3(untemper4(x))));
    }

    private static int untemper4(int x) {
        return x ^ (x >>> 18);
    }

    private static int untemper3(int x) {
        return x ^ ((x & 0x1df8c) << 15);
    }

    private static int untemper2(int x) {
        int[] masks = {0x2d, 0x31, 0x69, 0x9};
        for (int i = 0; i < 4; i++) {
            x ^= ((x >>> 7 * i) & masks[i]) << (7 * (i + 1));
        }
        return x;
    }

    public static int untemper1(int x) {
        x ^= (x >>> 11) & 0x1ffc00;
        return x ^ ((0x7FF<<10) & x) >>> 11;
    }
}
