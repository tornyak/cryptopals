package com.tornyak.cryptopals.basics;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class HexTest {

    @Test
    public void toBase64() {
        assertArrayEquals("".getBytes(), Hex.toBase64(""));
        assertArrayEquals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t".getBytes(),
                Hex.toBase64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
    }

    @Test
    public void charToByte() {
        char[] hexChars = "0123456789abcd".toCharArray();
        for (int i = 0; i < hexChars.length; i++) {
            assertEquals(i, Hex.charToByte(hexChars[i]));
        }
    }

    @Test
    public void charToByteCapitalLetters() {
        char[] hexChars = "0123456789ABCD".toCharArray();
        for (int i = 0; i < hexChars.length; i++) {
            assertEquals(i, Hex.charToByte(hexChars[i]));
        }
    }

    @Test
    public void xor() {
        String a = "1c0111001f010100061a024b53535009181c";
        String b = "686974207468652062756c6c277320657965";
        String result = "746865206b696420646f6e277420706c6179";
        assertEquals(result, Hex.xor(a, b));
        assertEquals("65", Hex.xor("37", "52"));
        assertEquals("37", Hex.xor("65", "52"));


    }

    @Test
    void stringFromBytes() {

    }

    @Test
    void stringToBytes() {
        assertEquals("e", new String(Hex.stringToBytes("65")));
    }

    @Test
    void isHex() {
        String hexChars = "0123456789abcdefABCDEF";
        for(char c : hexChars.toCharArray()) {
            assertTrue(Hex.isHex(c), "Failed for: " + c);
        }
    }

    @Test
    void byteToChar() {
    }
}