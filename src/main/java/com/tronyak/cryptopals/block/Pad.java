package com.tronyak.cryptopals.block;

import java.util.Arrays;

public class Pad {
    public static byte[] pkcs7(byte[] data, int blockSize) {
        if(blockSize < 1 || blockSize > 255) {
            throw new IllegalArgumentException("Block size out of range: " + blockSize);
        }
        int padLength = blockSize - data.length % blockSize;
        if(padLength == 0) {
            padLength = blockSize;
        }

        byte[] result = new byte[data.length + padLength];
        System.arraycopy(data, 0, result, 0, data.length);
        Arrays.fill(result, data.length, result.length, (byte)padLength);

        return result;
    }
}
