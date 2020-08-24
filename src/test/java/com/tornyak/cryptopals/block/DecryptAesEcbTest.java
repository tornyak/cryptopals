package com.tornyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Hex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecryptAesEcbTest {

    @Test
    @DisplayName("Set 2 challenge 12 - Byte-at-a-time ECB decryption (Simple)")
    void byteAtATimeEcbDecryptionSimple() {
        AesEcb cypher = new AesEcb();
        int blockSize = discoverBlockSize(cypher);
        int cyphertextSize = cypher.appendAndEncrypt("".getBytes()).length;
        assertEquals(16, blockSize);

        String base = "A".repeat(cyphertextSize - 1);
        String value = "";

        for (int i = 0; i < cyphertextSize; i++) {
            List<String> input = generateInput(base);
            Map<String, String> dictionary = input.stream()
                    .map(s -> Map.entry(s, cypher.appendAndEncrypt(s.getBytes())))
                    .collect(HashMap::new,
                            (m, e) -> m.put(Hex.stringFromBytes(Arrays.copyOf(e.getValue(), cyphertextSize)), e.getKey()),
                            Map::putAll);

            byte[] cyphertext = cypher.appendAndEncrypt("A".repeat(cyphertextSize - i - 1).getBytes());
            String newValue = dictionary.get(Hex.stringFromBytes(Arrays.copyOf(cyphertext, cyphertextSize)));
            if(newValue == null) {
                break;
            }
            value = newValue;
            base = value.substring(1);
        }

        value = value.trim().replaceFirst("A*", "");

        String expected = "Rollin' in my 5.0\n" +
                "With my rag-top down so my hair can blow\n" +
                "The girlies on standby waving just to say hi\n" +
                "Did you stop? No, I just drove by";

        assertEquals(expected, value);
    }

    @Test
    @DisplayName("Set 2 challenge 14 - Byte-at-a-time ECB decryption (Harder)")
    void byteAtATimeEcbDecryptionHarder() {
        AesEcb cypher = new AesEcb();
        int blockSize = 16;
        int cyphertextSize = cypher.prependAndEncrypt("".getBytes()).length;
        int prefixSize = discoverPrefixSize(cypher, blockSize);

        String base = "A".repeat(cyphertextSize - prefixSize - 1);
        String value = "";

        for (int i = 0; i < cyphertextSize; i++) {
            List<String> input = generateInput(base);
            Map<String, String> dictionary = input.stream()
                    .map(s -> Map.entry(s, cypher.prependAndEncrypt(s.getBytes())))
                    .collect(HashMap::new,
                            (m, e) -> m.put(Hex.stringFromBytes(Arrays.copyOf(e.getValue(), cyphertextSize)), e.getKey()),
                            Map::putAll);

            byte[] cyphertext = cypher.prependAndEncrypt("A".repeat(cyphertextSize - i - 1).getBytes());
            byte[] cyphertextBlockOfInterest = Arrays.copyOf(cyphertext, cyphertextSize);

            String newValue = dictionary.get(Hex.stringFromBytes(cyphertextBlockOfInterest));
            if (newValue == null) {
                break;
            }
            value = newValue;
            base = value.substring(1);
        }
        value = value.trim().replaceFirst("A*", "");

        String expected = "Rollin' in my 5.0\n" +
                "With my rag-top down so my hair can blow\n" +
                "The girlies on standby waving just to say hi\n" +
                "Did you stop? No, I just drove by";

        assertEquals(expected, value);
    }

    private int discoverPrefixSize(AesEcb cypher, int blockSize) {
        byte[] cyphertext = cypher.prependAndEncrypt("".getBytes());
        int equalBlocks = countEqualBlocks(cyphertext, blockSize);

        for (int i = 0; i < 1024; i++) {
            cyphertext = cypher.prependAndEncrypt("A".repeat(2 * blockSize + i).getBytes());
            int idx = consecutiveEqualBlocks(cyphertext, blockSize);
            if(idx > -1) {
                return idx - i;
            }
        }
        return -1;
    }

    /**
     * Return first index in b where same consecutive blocks start
     * @param b
     * @param blockSize
     * @return
     */
    private int consecutiveEqualBlocks(byte[] b, int blockSize) {
        int blocks = b.length / blockSize;
        for (int i = 0; i < blocks - 2; i++) {
            if(Arrays.equals(b, i * blockSize, (i + 1) * blockSize, b, (i + 1) * blockSize, (i+2) * blockSize)) {
                return i * blockSize;
            }
        }
        return -1;
    }


    private int countEqualBlocks(byte[] b, int blockSize) {
        int totalBlocks = b.length / blockSize;
        Set<String> uniqueBlocks = new HashSet<>();
        for (int i = 0; i < totalBlocks - 1; i++) {
            uniqueBlocks.add(Hex.stringFromBytes(Arrays.copyOfRange(b, i * blockSize, (i + 1) * blockSize)));
        }
        return totalBlocks - uniqueBlocks.size();
    }

    private List<String> generateInput(String base) {
        List<String> result = new ArrayList<>(128);
        StringBuffer sb = new StringBuffer(base.length() + 1);
        sb.append(base);
        sb.append("A");
        for (int c = 0; c <= 127 ; c++) {
            sb.setCharAt(base.length(), (char)c);
            result.add(sb.toString());
        }
        return result;
    }

    private int discoverBlockSize(AesEcb cypher) {
        for (int i = 2; i <= 256; i++) {
            byte[] cyphertext = cypher.appendAndEncrypt("A".repeat(i).getBytes());
            if(firstTwoBlocksEqual(cyphertext, i >> 1)) {
                return i >> 1;
            }
        }
        return -1;
    }

    private boolean firstTwoBlocksEqual(byte[] data, int blockSize) {
        return Arrays.compare(data, 0, blockSize, data, blockSize, 2 * blockSize) == 0;
    }
}
