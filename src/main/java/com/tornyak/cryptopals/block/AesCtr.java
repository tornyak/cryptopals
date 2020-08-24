package com.tornyak.cryptopals.block;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesCtr {
    public static final int BLOCK_SIZE = 16;
    private static final int NONCE_SIZE = 8;

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        Cipher cipher = initCipher(key, Cipher.ENCRYPT_MODE);
        byte[] result = new byte[data.length];
        byte[] encryptedIv = new byte[iv.length];
        byte[] plainIv = iv.clone();

        try {
            cipher.update(plainIv, 0, iv.length, encryptedIv, 0);

            for (int i = 0, ivIdx = 0; i < data.length; i++, ivIdx++) {
                if(ivIdx == BLOCK_SIZE) {
                    increment(plainIv);
                    cipher.update(plainIv, 0, iv.length, encryptedIv, 0);
                    ivIdx = 0;
                }
                result[i] = (byte)(data[i] ^ encryptedIv[ivIdx]);
            }
        } catch (ShortBufferException e) {
            throw new SecurityException(e);
        }

        return result;
    }

    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) {
        return encrypt(data, key, iv);
    }

    static byte[] increment(byte[] b) {
        for (int i = NONCE_SIZE; i < b.length && ++b[i] == 0; i++) ;
        return b;
    }

    private static Cipher initCipher(byte[] key, int mode) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(mode, keySpec);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new SecurityException(e);
        }
    }
}
