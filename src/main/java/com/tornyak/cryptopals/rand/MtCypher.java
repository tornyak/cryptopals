package com.tornyak.cryptopals.rand;

import com.tornyak.cryptopals.basics.Bytes;

public class MtCypher {

    private MtCypher() {
    }

    public static byte[] encrypt(byte[] data, byte[] seed) {
        if (data.length < 1) {
            return new byte[0];
        }
        MersenneTwisterRng rng = new MersenneTwisterRng(Bytes.toInteger(seed));
        BytesGenerator bytesGenerator = new BytesGenerator(rng);
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ bytesGenerator.next());
        }
        return result;
    }

    public static byte[] decrypt(byte[] data, byte[] seed) {
        return encrypt(data, seed);
    }

    private static class BytesGenerator {
        private MersenneTwisterRng rng;
        private int i = 4;
        private byte[] randomBytes;

        public BytesGenerator(MersenneTwisterRng rng) {
            this.rng = rng;
        }

        byte next() {
            if (i > 3) {
                randomBytes = Bytes.fromInteger((int) rng.nextInt32());
                i = 0;
            }
            return randomBytes[i++];
        }
    }
}
