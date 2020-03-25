package com.tornyak.cryptopals.basics;

public class Hex {
    private static final String HEX_CHARS = "0123456789abcdefABCDEF";

    private Hex(){}

    /**
     * Creates a String of HEX characters from a byte array. Every byte will result in two HEX chars
     *
     * @param bytes array of bytes
     * @return String of hex characters
     */
    public static String stringFromBytes(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            byte hex1 = (byte)((bytes[i] >> 4) & 0xF);
            byte hex2 = (byte)(bytes[i] & 0xF);
            hexChars[2*i] = byteToChar(hex1);
            hexChars[2*i + 1] = byteToChar(hex2);
        }
        return String.valueOf(hexChars);
    }


    /**
     * Creates a byte array from HEX String. Two HEX characters are packed into a byte.
     * If length of input String is odd last byte is filled with four 0 lower order bits
     * @param hex String of HEX characters
     * @return byte array that packs input HEX characters.
     */
    public static byte[] stringToBytes(String hex) {
        char[] hexChars = hex.toCharArray();
        byte[] hexBytes = new byte[(hexChars.length + 1) / 2];

        int remaining = hexChars.length;
        int resultIdx = 0;
        int i = 0;

        while(remaining > 1) {
            byte byte1 = charToByte(hexChars[i++]);
            byte byte2 = charToByte(hexChars[i++]);
            hexBytes[resultIdx++] = (byte)((byte1 << 4) | byte2 & 0xFF);
            remaining -= 2;
        }

        if(remaining > 0) {
            hexBytes[resultIdx] = (byte)(charToByte(hexChars[i]) << 4);
        }
        return hexBytes;
    }

    public static boolean isHex(char c) {
        return HEX_CHARS.indexOf(c) >= 0;
    }

    public static char byteToChar(byte b) {
        switch(b) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'a';
            case 11:
                return 'b';
            case 12:
                return 'c';
            case 13:
                return 'd';
            case 14:
                return 'e';
            case 15:
                return 'f';
            default:
                throw new IllegalArgumentException(String.valueOf(b));
        }
    }

    public static byte charToByte(char c) {
        switch (Character.toLowerCase(c)) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 10;
            case 'b':
                return 11;
            case 'c':
                return 12;
            case 'd':
                return 13;
            case 'e':
                return 14;
            case 'f':
                return 15;
            default:
                throw new IllegalArgumentException(String.valueOf(c));
        }
    }

    public static byte[] toBase64(String s) {
        return Base64.encode(stringToBytes(s));
    }

    public static String xor(String a, String b) {
        if(a.length() != b.length()) {
            throw new IllegalArgumentException("a and b must be equal size");
        }

        byte[] result = new byte[(a.length() + 1) / 2];
        byte[] aBytes = stringToBytes(a);
        byte[] bBytes = stringToBytes(b);
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte)(aBytes[i] ^ bBytes[i]);
        }
        return stringFromBytes(result);
    }
}
