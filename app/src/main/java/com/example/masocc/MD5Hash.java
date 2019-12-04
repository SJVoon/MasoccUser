package com.example.masocc;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
    public static String algorithm = "MD5";

    public static String encrypt(String message) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(message.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder sb= new StringBuilder();
            for (byte b: bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("H 15", e.toString());
        }
        return hash;
    }
}
