package com.kubuski.urlshortener.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import java.util.Base64;

public class KeyGeneratorUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);

        Key key = keyGen.generateKey();
        String base64EncodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Base64 Encoded Key: " + base64EncodedKey);
    }
}
