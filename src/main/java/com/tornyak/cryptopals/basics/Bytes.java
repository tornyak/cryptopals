package com.tornyak.cryptopals.basics;

import java.util.Arrays;

public class Bytes {

    public static byte[][] splitIntoBlocks(byte[] bytes, int size) {
        int blocks = (bytes.length + size - 1) / size;
        byte[][] result = new byte[blocks][size];
        int remaining = bytes.length;
        for (int i = 0; (i < blocks - 1) && (remaining >= size); i++) {
            result[i] = Arrays.copyOfRange(bytes, size * i, size * (i + 1));
            remaining-=size;
        }
//        if(remaining > 0) {
//            result[blocks - 1] = new byte[size];
//            System.arraycopy(bytes, size * (blocks - 1), result[blocks - 1], 0, remaining);
//        }
        return result;
    }

    public static byte[][] transpose(byte[][] b) {
        byte[][] result = new byte[b[0].length][b.length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                result[j][i] = b[i][j];
            }
        }
        return result;
    }
}
