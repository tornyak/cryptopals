package com.tornyak.cryptopals.block;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AesEcb {
    private static final SecureRandom rng = new SecureRandom();
    private byte[] key = generateKey(16);
    private byte[] append = Base64.getMimeDecoder()
            .decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg\n" +
                    "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq\n" +
                    "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg\n" +
                    "YnkK");

    private byte[] prefix = generatePrefix();

    private byte[] generatePrefix() {
        int cnt = rng.nextInt(32);
        byte[] result = new byte[cnt];
        rng.nextBytes(result);
        return result;
    }

    public byte[] appendAndEncrypt(byte[] data) {
        byte[] appendedData = new byte[data.length + append.length];
        System.arraycopy(data, 0, appendedData, 0, data.length);
        System.arraycopy(append, 0, appendedData, data.length, append.length);
        return encrypt(appendedData, key);
    }

    public byte[] prependAndEncrypt(byte[] data) {
        byte[] appendedDataWithPrefix = new byte[data.length + prefix.length + append.length];
        System.arraycopy(prefix, 0, appendedDataWithPrefix, 0, prefix.length);
        System.arraycopy(data, 0, appendedDataWithPrefix, prefix.length, data.length);
        System.arraycopy(append, 0, appendedDataWithPrefix, prefix.length + data.length, append.length);
        return encrypt(appendedDataWithPrefix, key);
    }


    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = initCipher(key, Cipher.ENCRYPT_MODE);
            int blockSize = key.length;
            byte[] paddedData = Pad.pkcs7(data, blockSize);
            return cipher.doFinal(paddedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new SecurityException(e);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = initCipher(key, Cipher.DECRYPT_MODE);
            byte[] plaintext = cipher.doFinal(data);
            return Pad.removePkcs7(plaintext);
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

    private byte[] generateKey(int keySizeBytes) {
        byte[] key = new byte[keySizeBytes];
        rng.nextBytes(key);
        return key;
    }
}
