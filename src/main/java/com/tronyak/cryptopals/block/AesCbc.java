package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesCbc {
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            Cipher cipher = initCipher(key, Cipher.ENCRYPT_MODE);

            final int blockSize = key.length;
            byte[] paddedData = Pad.pkcs7(data, blockSize);
            final byte[] ciphertext = new byte[paddedData.length];

            byte[] input = Bytes.xor(data, 0, iv, 0, blockSize);
            cipher.update(input, 0, blockSize, ciphertext, 0);

            for (int i = 1; i < paddedData.length / blockSize; i++) {
                input = Bytes.xor(data, i, ciphertext, i - 1, blockSize);
                cipher.update(input, 0, blockSize, ciphertext, i * blockSize);
            }

            cipher.doFinal();
            return ciphertext;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | ShortBufferException e) {
            throw new SecurityException(e);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            Cipher cipher = initCipher(key, Cipher.DECRYPT_MODE);
            final int blockSize = key.length;
            final byte[] plaintext = new byte[data.length];

            byte[] decryptedBlock = cipher.update(data, 0, blockSize);
            byte[] xor = Bytes.xor(decryptedBlock, 0, iv, 0, blockSize);
            System.arraycopy(xor, 0, plaintext, 0, blockSize);

            for (int i = 1; i < plaintext.length / blockSize; i++) {
                decryptedBlock = cipher.update(data, i * blockSize, blockSize);
                xor = Bytes.xor(decryptedBlock, 0, data, (i - 1) * blockSize, blockSize);
                System.arraycopy(xor, 0, plaintext, (i - 1) * blockSize, blockSize);
            }

            cipher.doFinal();
            return plaintext;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new SecurityException(e);
        }
    }

    private static Cipher initCipher(byte[] key, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(mode, keySpec);
        return cipher;
    }
}
