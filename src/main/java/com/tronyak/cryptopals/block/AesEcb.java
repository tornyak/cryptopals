package com.tronyak.cryptopals.block;

import com.tornyak.cryptopals.basics.Bytes;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesEcb {
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
}
