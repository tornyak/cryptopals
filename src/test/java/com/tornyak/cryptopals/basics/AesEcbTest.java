package com.tornyak.cryptopals.basics;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AesEcbTest {

    @Test
    @DisplayName("Set 1 challenge 7 - AES in ECB mode")
    void decryptFile() throws Exception {
        URI uri = getClass().getClassLoader().getResource("challenge7_data.txt").toURI();
        Path path = Paths.get(uri);

        String key = "YELLOW SUBMARINE";
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] base64encoded = Files.readAllBytes(path);
        byte[] ciphertext = Base64.getMimeDecoder().decode(base64encoded);
        byte[] plaintext = cipher.doFinal(ciphertext);

        assertTrue(new String(plaintext).trim().endsWith("Play that funky music"));
    }

    @Test
    @DisplayName("Set 1 challenge 8 - Detect AES in ECB mode")
    void detectEcb() throws Exception {
        URI uri = getClass().getClassLoader().getResource("challenge8_data.txt").toURI();
        Path path = Paths.get(uri);
        List<String> hexLines = Files.readAllLines(path);
        Pair<String,Integer> ecbEncrypted = hexLines.stream()
                .map(this::countEqualBlocks)
                .filter(p -> p.getRight() > 0)
                .findFirst()
                .get();

        assertTrue(ecbEncrypted.getLeft().startsWith("d880619740a8a19b7840a8a31c810a3d08649af70dc06f4fd5d2d69c744cd283"));
    }

    private Pair<String, Integer> countEqualBlocks(String s) {
        int blockSize = 32; // 16 bytest * 2 bytes per HEX digit
        int blockCnt = s.length() / blockSize;
        Set<String> uniqueBlocks = new HashSet<>();

        for (int i = 0; i < blockCnt; i++) {
            uniqueBlocks.add(s.substring(i * blockSize, (i+1) * blockSize));
        }
        return Pair.of(s, blockCnt - uniqueBlocks.size());
    }
}
