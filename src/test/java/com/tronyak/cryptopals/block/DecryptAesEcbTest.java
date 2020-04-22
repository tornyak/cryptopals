package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Hex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecryptAesEcbTest {

    @Test
    @DisplayName("Set 2 challenge 12 - Byte-at-a-time ECB decryption (Simple)")
    void byteAtATimeEcbDecryptionSimple() {
        AesEcb cypher = new AesEcb();
        int blockSize = discoverBlockSize(cypher);
        assertEquals(16, blockSize);

        String base = "A".repeat(blockSize - 1);
        String value = "";

        for (int i = 0; i < blockSize; i++) {
            List<String> input = generateInput(base);
            Map<String, String> dictionary = input.stream()
                    .map(s -> Map.entry(s, cypher.appendAndEncrypt(s.getBytes())))
                    .collect(HashMap::new,
                            (m, e) -> m.put(Hex.stringFromBytes(Arrays.copyOf(e.getValue(), blockSize)), e.getKey()),
                            Map::putAll);

            byte[] cyphertext = cypher.appendAndEncrypt("A".repeat(blockSize - i - 1).getBytes());
            byte[] firstCyphertextBlock = Arrays.copyOf(cyphertext, blockSize);

            value = dictionary.get(Hex.stringFromBytes(firstCyphertextBlock));
            base = value.substring(1);
            System.out.println("i=" + i + " value=" + value + " base=" + base);
        }

        assertEquals("Rollin' in my 5.", value);


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
