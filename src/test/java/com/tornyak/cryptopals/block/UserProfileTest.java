package com.tornyak.cryptopals.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserProfileTest {

    @Test
    void parseUserProfile() {
        UserProfile up = UserProfile.forEmail("ninja@zilla.com");
        assertAll(
                () -> assertEquals("ninja@zilla.com", up.email),
                () -> assertEquals("user", up.role)
        );
    }

    @Test
    void encode() {
        String encoded = "email=ninja@zilla.com&uid=%s&role=user";
        UserProfile up = UserProfile.forEmail("ninja@zilla.com");
        assertEquals(String.format(encoded, up.uid), up.toString());
    }

    @Test
    @DisplayName("Set 2 challenge 13 - ECB cut-and-paste")
    void ecbUserProfile() {
        int blockSize = 16;
        byte[] key = generateKey(blockSize);
        String email = "x".repeat(10) + new String(Pad.pkcs7("admin".getBytes(), blockSize));
        byte[] adminEmail = AesEcb.encrypt(UserProfile.forEmail(email).toString().getBytes(), key);


        byte[] ninjaProfile = AesEcb.encrypt(UserProfile.forEmail("ninja@aaa.com").toString().getBytes(), key);
        System.arraycopy(adminEmail, 16, ninjaProfile, 32, blockSize);

        final byte[] plaintext = AesEcb.decrypt(ninjaProfile, key);
        assertEquals("email=ninja@aaa.com&uid=10&role=admin", new String(plaintext));
    }

    private byte[] generateKey(int size) {
        byte[] key = new byte[size];
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(key);
        return key;
    }

}