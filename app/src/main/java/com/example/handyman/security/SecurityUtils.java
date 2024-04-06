package com.example.handyman.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// -------------- for password encryption --------------
public class SecurityUtils {

    private static final String PEPPER = "handyman"; //secret key - This should be kept secret and not exposed.

    public static String hashPassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // encryption algorithm
            md.update(PEPPER.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
