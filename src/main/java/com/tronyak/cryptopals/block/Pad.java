package com.tronyak.cryptopals.block;

import javax.crypto.BadPaddingException;
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

    public static byte[] removePkcs7(byte[] data) throws BadPaddingException {
        validatePkcs7(data);
        int zeroCnt = countPaddingZeros(data);
        byte padByte = data[data.length - 1 -zeroCnt];
        return Arrays.copyOf(data, data.length - zeroCnt - padByte);
    }

    public static void validatePkcs7(byte[] data) throws BadPaddingException {
        byte padByte = data[data.length - 1];
        if(padByte <= 0 || padByte > 16) {
            throw new BadPaddingException("Pad value out of range: " + padByte);
        }
        for (int i = 1; i < padByte; i++) {
            if(data[data.length - 1 - i] != padByte) {
                throw new BadPaddingException("Bad pad");
            }
        }
    }

    public static byte[] removeZeros(byte[] data) {
        int zeroCount = countPaddingZeros(data);
        return Arrays.copyOf(data, data.length - zeroCount);
    }

    static int countPaddingZeros(byte[] data) {
        int i = data.length - 1;
        while (i >= 0 && data[i] == 0) {
            i--;
        }
        return data.length -1 - i;
    }
}
