package com.tronyak.cryptopals.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AesCbcTest {

    @Test
    @DisplayName("Set 2 challenge 10 - Implement CBC mode")
    void decrypt() throws Exception {
        int blockSize = 16;
        byte[] key = "YELLOW SUBMARINE".getBytes();

        URI uri = getClass().getClassLoader().getResource("challenge10_data.txt").toURI();
        Path path = Paths.get(uri);
        byte[] base64encoded = Files.readAllBytes(path);
        byte[] ciphertext = Base64.getMimeDecoder().decode(base64encoded);

        byte[] iv = generateIv(blockSize);

        final byte[] plaintext = AesCbc.decrypt(ciphertext, key, iv);
        assertTrue(new String(plaintext).trim().endsWith("Play that funky music"));
    }

    @Test
    void encryptDecrypt() throws Exception {
        int blockSize = 16;
        byte[] key = "YELLOW SUBMARINE".getBytes();
        byte[] iv = generateIv(blockSize);

        String plaintext =
                "Una mattina mi son alzato,\n" +
                "o bella ciao, bella ciao, bella ciao ciao ciao!\n" +
                "Una mattina mi son alzato\n" +
                "e ho trovato l'invasor.";

        byte[] ptBytes = plaintext.getBytes();

        assertArrayEquals(ptBytes, AesCbc.decrypt(AesCbc.encrypt(ptBytes, key, iv), key, iv));
    }

    private byte[] generateIv(int size) {
        byte[] iv = new byte[size];
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(iv);
        return iv;
    }


}