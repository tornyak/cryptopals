package com.tornyak.cryptopals.basics;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XorCipherTest {

    @Test
    public void encrypt() {
        String hexMessage = Hex.stringFromBytes("This is the message!".getBytes());
        for(int k = 0; k < 65535; k++) {
            byte key = (byte)k;
            assertEquals(hexMessage, XorCipher.decrypt(XorCipher.encrypt(hexMessage, key), key));
        }
    }

    @Test
    @DisplayName("Set 1 challenge 3 - Single-byte XOR cipher")
    void findKeyAndDecrypt() {
        final String ciphertext = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        Pair<Byte, Double> keyDistancePair = findBestKeyFreqSquareSum(ciphertext);
        byte key = keyDistancePair.getLeft();
        final String plaintext = Hex.toReadableString(XorCipher.decrypt(ciphertext, key));
        assertAll(
                () -> assertEquals(88, key),
                () -> assertEquals("Cooking MC's like a pound of bacon", plaintext));
    }

     @Test
     @DisplayName("Set 1 challenge 4 - Detect single-character XOR")
    void detectSingleCharXor() throws Exception {
         URI uri = getClass().getClassLoader().getResource("challenge4_data.txt").toURI();
         File file = new File(uri);

         final List<String> ciphertexts = Files.readAllLines(file.toPath());
             ciphertexts.forEach(c -> {
                 Pair<Byte, Double> keyDistancePair = findBestKeyFreqSquareSum(c);
                 byte key = keyDistancePair.getLeft();
                 double distance = keyDistancePair.getRight();
                 final String plaintext = Hex.toReadableString(XorCipher.decrypt(c, key));
                 if(distance < 0.1) {
                    System.out.printf("key: %d dist: %3.3f : %s -> %s\n", key, distance, c, plaintext);
                 }
             });
     }

    @Test
    @DisplayName("Set 1 challenge 5 - Implement repeating-key XOR")
    void repeatingKeyEncrypt() {
        String plaintext = "Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal";
        String expectedCiphertext = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";

        assertEquals(expectedCiphertext, XorCipher.encrypt(plaintext, "ICE"));
        assertEquals(plaintext, XorCipher.decrypt(Hex.stringToBytes(expectedCiphertext), "ICE"));

    }

    @Test
    @DisplayName("Set 1 challenge 6 - Break repeating-key XOR")
    void breakRepeatingKeyXor() throws Exception {
        URI uri = getClass().getClassLoader().getResource("challenge6_data.txt").toURI();
        Path path = Paths.get(uri);
        byte[] bytes = Base64.getMimeDecoder().decode(Files.readAllBytes(path));

        TreeMap<Double, Integer> distances = calculateKeyDistances(bytes);
        int keyLength = distances
                .descendingKeySet()
                .stream()
                .map(distances::get)
                .findFirst().get();

        String key = findKeyOfSize(keyLength, bytes);
        assertEquals("Terminator X: Bring the noise", key);
    }

    public static String findKeyOfSize(int keySize, byte[] bytes) {
        byte[][] blocks = Bytes.splitIntoBlocks(bytes, keySize);
        byte[][] transposed = Bytes.transpose(blocks);
        StringBuilder key = new StringBuilder();

        for(int c = 0; c < keySize; c++) {
            final Pair<Byte, Double> keyFreqSum = findBestKeyFreqSquareSum(Hex.stringFromBytes(transposed[c]));
            key.append(Character.toString(keyFreqSum.getLeft()));
        }
        return key.toString();
    }

    private static Pair<Byte, Double> findBestKeyFreqSquareSum(String s) {
        final double englishFreqSum = 0.065;
        double minDelta = Double.MAX_VALUE;
        int key = 0;

        for (int k = 0; k < 255; k++) {
            String hexPlaintext = XorCipher.decrypt(s, (byte) k);
            String plaintext = new String(Hex.stringToBytes(hexPlaintext));
            if(getLetterPercentage(plaintext) < 0.90) {
                continue;
            }
            double freqSum = Statistics.sumOfLetterFrequencySquares(plaintext);
            double delta = Math.abs(freqSum - englishFreqSum);
            if(isStringPrintable(plaintext)) {
                if (delta < minDelta) {
                    key = k;
                    minDelta = delta;
                }
            }
        }
        return Pair.of((byte)key, minDelta);
    }

    private static double getLetterPercentage(String plaintext) {
        IntPredicate isAlphabetic = Character::isAlphabetic;
        IntPredicate isSpace = Character::isWhitespace;
        return (double)plaintext.chars().filter(isAlphabetic.or(isSpace)).count() / plaintext.length();
    }


    private TreeMap<Double, Integer> calculateKeyDistances(byte[] bytes) {
        TreeMap<Double, Integer> distances = new TreeMap<>(Comparator.reverseOrder());

        for (int keySize = 2; keySize <= 40 ; keySize++) {
            double distance = calculateKeyDistance(bytes, keySize);
            distances.put(distance, keySize);
        }
        return distances;
    }

    private double calculateKeyDistance(byte[] bytes, int keySize) {
        final int sampleSize = 10;
        byte[] prevBytes = Arrays.copyOfRange(bytes, 0, keySize);
        double totalDistance = 0;

        for (int i = 1; i < sampleSize; i++) {
            byte[] nextBytes = Arrays.copyOfRange(bytes, i * keySize, (i + 1) * keySize);
            totalDistance += Statistics.hummingDistance(new String(prevBytes), new String(nextBytes)) / (double)keySize;
            prevBytes = nextBytes;
        }
        return totalDistance / (sampleSize -1);
    }

    private static boolean isStringPrintable(String s) {
        IntPredicate isWhitespace = Character::isWhitespace;
        IntPredicate isAsciiPrintable = i -> CharUtils.isAsciiPrintable((char)i);
        return s.chars().filter(isAsciiPrintable.or(isWhitespace).negate()).findAny().isEmpty();
    }

}