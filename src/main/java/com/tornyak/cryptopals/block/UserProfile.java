package com.tornyak.cryptopals.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProfile {
    String email;
    String uid;
    String role;

    protected UserProfile(String email, String uid, String role) {
        this.email = email;
        this.uid = uid;
        this.role = role;
    }

    public static UserProfile forEmail(String email) {
        if(email.contains("&") || email.contains("=")) {
            throw new IllegalArgumentException("email must not contain '&' or '='");
        }
        return  new UserProfile(email, "10", "user");
    }

    public static Map<String, String> parse(String data) {
        Map<String, String> result = new HashMap<>();
        String[] kvPairs = data.split("&");
        for(String s : kvPairs) {
            String[] kv = s.split("=");
            result.put(kv[0], kv[1]);
        }
        return result;
    }

    @Override
    public String toString() {
        return String.join("&", "email=" + email, "uid=" + uid, "role=" + role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, uid, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return email.equals(that.email) &&
                uid.equals(that.uid) &&
                role.equals(that.role);
    }
}
