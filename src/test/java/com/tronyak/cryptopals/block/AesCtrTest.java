package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;
import com.tornyak.cryptopals.basics.NGrams;
import com.tornyak.cryptopals.basics.XorCipher;
import com.tornyak.cryptopals.basics.XorCipherTest;
import org.apache.commons.lang3.CharUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

class AesCtrTest {

    private static int blockSize = 16;
    private static NGrams nGrams;

    @BeforeAll
    static void setUp() throws Exception {
        Path[] paths = List.of("ngrams1.txt", "ngrams2.txt", "ngrams3.txt")
                .stream()
                .map(AesCtrTest::getPathForResource)
                .toArray(Path[]::new);

        nGrams = NGrams.loadFromFiles(paths);
    }

    @Test
    @DisplayName("Set 3 challenge 18 - Implement CTR")
    void decryptText() {
        byte[] key = "YELLOW SUBMARINE".getBytes();
        byte[] iv = Bytes.filledWithValue(blockSize, (byte) 0);
        byte[] ciphertext = Base64.getDecoder().decode("L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ==");

        final byte[] plaintext = AesCtr.decrypt(ciphertext, key, iv);

        assertEquals("Yo, VIP Let's kick it Ice, Ice, baby Ice, Ice, baby ", new String(plaintext));
    }

    @ParameterizedTest
    @MethodSource("ctrArguments")
    void incrementCounter(byte[] ctr, byte[] incCtr) {
        assertArrayEquals(incCtr, AesCtr.increment(ctr));
    }

    static Stream<Arguments> ctrArguments() {
        return Stream.of(
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, 0, 0, 0, 0, 0, 0},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF}),
                Arguments.of(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF},
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})
        );
    }

    @Test
    @DisplayName("Set 3 challenge 19 - Break fixed-nonce CTR mode using substitutions")
    void breakFixedNonceUsingSubstitutions() throws IOException {
        Path path = getPathForResource("challenge19_data.txt");
        final List<String> base64Plaintexts = Files.readAllLines(path);
        byte[] key = Bytes.random(blockSize);
        byte[] iv = new byte[blockSize];
        List<byte[]> ciphertexts = base64Plaintexts.stream()
                .map(Base64.getDecoder()::decode)
                .map(p -> AesCtr.encrypt(p, key, iv))
                .collect(toList());

        int longestLength = ciphertexts.stream().mapToInt(a -> a.length).max().getAsInt();

        TreeMap<Double, byte[]> keyStreams = new TreeMap<>();
        for (int i = 0; i < longestLength; i++) {
            keyStreams = addByteAndRecreateKeyStreams(ciphertexts, keyStreams, 100);
        }

        final Map.Entry<Double, byte[]> entry = keyStreams.pollLastEntry();

        String decrypted = new String(XorCipher.decryptTillCommonLength(ciphertexts.get(0), entry.getValue()));
        assertEquals("I have met them at close of day", decrypted);
    }

    private TreeMap<Double, byte[]> addByteAndRecreateKeyStreams(List<byte[]> ciphertexts, TreeMap<Double, byte[]> keys, int limit) {
        TreeMap<Double, byte[]> newKeys = new TreeMap<>();
        for (int j = 0; j < 256; j++) {
            TreeMap<Double, byte[]> combined = combineWithKeystreams((byte) j, keys);
            combined.forEach((fitness, key) -> {
                double newFitness = recalculateFitnessForKey(ciphertexts, fitness, key);
                if (fitness > Double.NEGATIVE_INFINITY) {
                    newKeys.put(newFitness, key);
                    if (newKeys.size() > limit) {
                        newKeys.pollFirstEntry();
                    }
                }
            });
        }
        return newKeys;
    }

    private TreeMap<Double, byte[]> combineWithKeystreams(byte k, TreeMap<Double, byte[]> keys) {
        TreeMap<Double, byte[]> result = new TreeMap<>();
        if (keys.size() == 0) {
            result.put(0.0, new byte[]{k});
            return result;
        }

        keys.forEach((fitness, key) -> {
            result.put(fitness, Bytes.append(key, k));
        });

        return result;
    }

    private double recalculateFitnessForKey(List<byte[]> ciphertexts, double oldFitness, byte[] key) {
        double result = 0.0;

        for (byte[] ciphertext : ciphertexts) {
            final double fitness = fitnessForKey(ciphertext, oldFitness, key);
            if (Double.isInfinite(fitness)) {
                return fitness;
            }
            result += fitness;
        }
        return result / ciphertexts.size();
    }

    private double fitnessForKey(byte[] ciphertext, double oldFitness, byte[] key) {
        byte[] plaintext = XorCipher.decryptTillCommonLength(ciphertext, key);
        return fitnessOfPlaintext(plaintext, oldFitness);
    }

    private double fitnessOfPlaintext(byte[] plaintext, double oldFitness) {
        double result = oldFitness;

        if (plaintext.length == 1) {
            return fitnessOfFirstCharacter(plaintext);
        }

        result += fitnessByFrequency(Arrays.copyOfRange(plaintext, plaintext.length - 2, plaintext.length));

        if (plaintext.length > 2) {
            result += fitnessByFrequency(Arrays.copyOfRange(plaintext, plaintext.length - 3, plaintext.length));
        }

        return result;
    }

    private double fitnessOfFirstCharacter(byte[] plaintext) {
        double fitness = 0;
        char c = (char) plaintext[0];

        if (CharUtils.isAsciiAlphaUpper(c)) {
            fitness += 100.0;
        }

        return fitness + fitnessByFrequency(plaintext);
    }

    private double fitnessByFrequency(byte[] plaintext) {
        double fitness = 0.0;
        String s = new String(plaintext);
        if (s.contains(" ")) {
            fitness += 10;
        }
        if (Character.isUpperCase(s.charAt(s.length() - 1))) {
            fitness -= 10;
        }
        return fitness + (10000 * nGrams.getFrequency(s));
    }

    @Test
    @DisplayName("Set 3 challenge 20 - Break fixed-nonce CTR statistically")
    void breakFixedNonceStatistically() throws Exception {
        Path path = getPathForResource("challenge20_data.txt");

        List<byte[]> ciphertexts = Files.readAllLines(path)
                .stream()
                .map(Base64.getDecoder()::decode)
                .collect(toList());

        int minLength = ciphertexts
                .stream()
                .mapToInt(c -> c.length)
                .min().getAsInt();

        byte[] trimmedAndJoinedCiphertext = ciphertexts
                .stream()
                .map(c -> Arrays.copyOf(c, minLength))
                .reduce(new byte[0], Bytes::append);

        String key = XorCipherTest.findKeyOfSize(minLength, trimmedAndJoinedCiphertext, 0.95);
        String plaintext = XorCipher.decrypt(trimmedAndJoinedCiphertext, key);
        assertTrue(plaintext.startsWith("I'm rated \"R\"...this is a warning, ya better void"));
    }

    private static Path getPathForResource(String filename) {
        URL url = ClassLoader.getSystemResource(filename);
        return Path.of(urlToUri(url));
    }

    private static URI urlToUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

}