package com.tornyak.cryptopals.basics;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base64Test {

    @Test
    public void encode() {
        assertArrayEquals("".getBytes(), Base64.encode("".getBytes()));
        assertArrayEquals("4oKs".getBytes(), Base64.encode("€".getBytes()));
        assertArrayEquals("YQ==".getBytes(), Base64.encode("a".getBytes()));
        assertArrayEquals("YWI=".getBytes(), Base64.encode("ab".getBytes()));
        assertArrayEquals("YWJj".getBytes(), Base64.encode("abc".getBytes()));
        assertArrayEquals("YWJjZA==".getBytes(), Base64.encode("abcd".getBytes()));
        assertArrayEquals("YWJjZGU=".getBytes(), Base64.encode("abcde".getBytes()));
        assertArrayEquals("YWJjZGVm".getBytes(), Base64.encode("abcdef".getBytes()));
        assertArrayEquals("YWJjZGVmZw==".getBytes(), Base64.encode("abcdefg".getBytes()));
    }

    @Test
    public void calculateSize() {
        assertEquals(0, Base64.calculateSize(0));
        assertEquals(4, Base64.calculateSize(1));
        assertEquals(4, Base64.calculateSize(2));
        assertEquals(4, Base64.calculateSize(3));
        assertEquals(8, Base64.calculateSize(4));
        assertEquals(8, Base64.calculateSize(6));
        assertEquals(12, Base64.calculateSize(7));
    }

    @Test
    public void encodeThreeBytes() {
        for(int i = 0; i < 255; i++) {
            for(int j = 0; j < 255; j++) {
                for(int k = 0; k < 255; k++) {
                    final var src = new byte[]{(byte)i, (byte)j, (byte)k};
                    var dst = new byte[4];
                    Base64.encodeThreeBytes(src, 0, dst, 0);
                    assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
                }
            }
        }
    }

    @Test
    public void encodeTwoBytes() {
        for(int i = 0; i < 255; i++) {
            for(int j = 0; j < 255; j++) {
                final var src = new byte[]{(byte)i, (byte)j};
                var dst = new byte[4];
                Base64.encodeTwoBytes(src, 0, dst, 0);
                assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
            }
        }
    }

    @Test
    public void encodeOneByte() {
        for(int i = 0; i < 255; i++) {
            final var src = new byte[]{(byte)i};
            var dst = new byte[4];
            Base64.encodeOneByte(src, 0, dst, 0);
            assertArrayEquals(java.util.Base64.getEncoder().encode(src), dst);
        }
    }
}