package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AesCbcTest {

    private static final String PREFIX = "comment1=cooking%20MCs;userdata=";
    private static final String SUFFIX = ";comment2=%20like%20a%20pound%20of%20bacon";
    private static final int blockSize = 16;

    @Test
    @DisplayName("Set 2 challenge 10 - Implement CBC mode")
    void decrypt() throws Exception {
        int blockSize = 16;
        byte[] key = "YELLOW SUBMARINE".getBytes();

        URI uri = getClass().getClassLoader().getResource("challenge10_data.txt").toURI();
        Path path = Paths.get(uri);
        byte[] base64encoded = Files.readAllBytes(path);
        byte[] ciphertext = Base64.getMimeDecoder().decode(base64encoded);
        byte[] iv = Bytes.random(blockSize);

        final byte[] plaintext = AesCbc.decrypt(ciphertext, key, iv);
        assertTrue(new String(plaintext).trim().endsWith("Play that funky music"));
    }

    @Test
    void encrypt() throws Exception {
        int blockSize = 16;
        byte[] key = "YELLOW SUBMARINE".getBytes();
        byte[] iv = Bytes.random(blockSize);

        String plaintext =
                "Una mattina mi son alzato,\n" +
                "o bella ciao, bella ciao, bella ciao ciao ciao!\n" +
                "Una mattina mi son alzato\n" +
                "e ho trovato l'invasor.";

        byte[] ptBytes = plaintext.getBytes();
        var aesCbcCipher = initAesCbcCipher(key, iv, Cipher.ENCRYPT_MODE);

        assertArrayEquals(aesCbcCipher.doFinal(ptBytes), AesCbc.encrypt(ptBytes, key, iv));
    }

    @Test
    void encryptDecrypt() {
        byte[] key = "YELLOW SUBMARINE".getBytes();
        byte[] iv = Bytes.random(blockSize);

        String plaintext =
                "Una mattina mi son alzato,\n" +
                "o bella ciao, bella ciao, bella ciao ciao ciao!\n" +
                "Una mattina mi son alzato\n" +
                "e ho trovato l'invasor.";

        byte[] ptBytes = plaintext.getBytes();

        assertArrayEquals(ptBytes, AesCbc.decrypt(AesCbc.encrypt(ptBytes, key, iv), key, iv));
    }

    private Cipher initAesCbcCipher(byte[] key, byte[] iv, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, keySpec, ivSpec);
        return cipher;

    }

    @Test
    @DisplayName("Set 2 challenge 16 - CBC bitflipping attacks")
    void bitflippingAttacks() {
        int blockSize = 16;
        byte[] key = Bytes.random(blockSize);
        byte[] iv = Bytes.random(blockSize);

        int prefixOverflow = PREFIX.getBytes().length % blockSize;
        int padLength = (prefixOverflow == 0) ? 0 : blockSize - prefixOverflow;
        String plaintext = "x".repeat(padLength) + "xxxxx:admin<true";
        byte[] ciphertext = prependAppendEncrypt(plaintext, key, iv.clone());

        int byteIndexColon = PREFIX.getBytes().length + padLength + 5 - blockSize;
        int byteIndexEquals = PREFIX.getBytes().length + padLength + 11 - blockSize;
        ciphertext[byteIndexColon] = Bytes.flipBit(0, ciphertext[byteIndexColon]);
        ciphertext[byteIndexEquals] = Bytes.flipBit(0, ciphertext[byteIndexEquals]);

        assertTrue(containsAdmin(ciphertext, key, iv.clone()));
    }

    byte[] prependAppendEncrypt(String plaintext, byte[] key, byte[] iv) {
        final int blockSize = key.length;
        String input = PREFIX + sanitize(plaintext) + SUFFIX;
        byte[] paddedData = Pad.pkcs7(input.getBytes(), blockSize);
        return AesCbc.encrypt(paddedData, key, iv);
    }

    private String sanitize(String plaintext) {
        String blackList = ";%";
        String result = plaintext;
        for (int i = 0; i < blackList.length(); i++) {
            char c = blackList.charAt(i);
            result = plaintext.replaceAll(Character.toString(c), "%" + (int)c);
        }
        return result;
    }

    boolean containsAdmin(byte[] ciphertext, byte[] key, byte[] iv) {
        var plaintext = new String(AesCbc.decrypt(ciphertext, key, iv));
        System.out.println(plaintext);
        var kvPairs = parseKeyValuePairs(plaintext, ";");
        return kvPairs.containsKey("admin");
    }

    public static Map<String, String> parseKeyValuePairs(String data, String delimiter) {
        Map<String, String> result = new HashMap<>();
        String[] kvPairs = data.split(delimiter);
        for(String s : kvPairs) {
            String[] kv = s.split("=");
            result.put(kv[0], kv[1]);
        }
        return result;
    }
}