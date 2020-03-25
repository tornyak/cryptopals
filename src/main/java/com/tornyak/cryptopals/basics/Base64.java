package com.tornyak.cryptopals.basics;

public class Base64 {
    private static final byte PADDING = '=';
    private static final char[] MAPPINGS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private Base64(){}

    public static byte[] encode(byte[] src) {
        byte[] dst = new byte[calculateSize(src.length)];

        int remainingBytes = src.length;
        int dstIndex = 0;
        int srcIndex = 0;

        while (remainingBytes >= 3) {
            encodeThreeBytes(src, srcIndex, dst, dstIndex);
            srcIndex+=3;
            dstIndex+=4;
            remainingBytes-=3;
        }

        if(remainingBytes == 2) {
            encodeTwoBytes(src, src.length - 2, dst, dstIndex);
        } else if (remainingBytes == 1) {
            encodeOneByte(src, src.length - 1, dst, dstIndex);
        }

        return dst;
    }

    static int calculateSize(int inputLength) {
        return  (inputLength + 2) / 3 * 4;
    }

    static void encodeThreeBytes(byte[] src, int srcIndex, byte[] dst, int dstIndex) {
        dst[dstIndex] = (byte)MAPPINGS[(src[srcIndex] & 0xFF) >> 2];
        dst[dstIndex + 1] = (byte)MAPPINGS[((src[srcIndex] & 0x3) << 4) | ((src[srcIndex + 1] & 0xFF) >> 4)];
        dst[dstIndex + 2] = (byte)MAPPINGS[((src[srcIndex + 1] & 0xF) << 2) | ((src[srcIndex + 2] & 0xFF) >> 6)];
        dst[dstIndex + 3] = (byte)MAPPINGS[src[srcIndex + 2] & 0x3F];
    }

    static void encodeTwoBytes(byte[] src, int srcIndex, byte[] dst, int dstIndex) {
        dst[dstIndex] = (byte)MAPPINGS[(src[srcIndex] & 0xFF) >> 2];
        dst[dstIndex + 1] = (byte)MAPPINGS[((src[srcIndex] & 0x3) << 4) | ((src[srcIndex + 1] & 0xFF) >> 4)];
        dst[dstIndex + 2] = (byte)MAPPINGS[((src[srcIndex + 1] & 0xF) << 2)];
        dst[dstIndex + 3] = PADDING;
    }

    static void encodeOneByte(byte[] src, int srcIndex, byte[] dst, int dstIndex) {
        dst[dstIndex] = (byte)MAPPINGS[(src[srcIndex] & 0xFF) >> 2];
        dst[dstIndex + 1] = (byte)MAPPINGS[((src[srcIndex] & 0x3) << 4)];
        dst[dstIndex + 2] = PADDING;
        dst[dstIndex + 3] = PADDING;
    }

}
