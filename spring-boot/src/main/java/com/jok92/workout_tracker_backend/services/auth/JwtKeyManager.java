package com.jok92.workout_tracker_backend.services.auth;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JwtKeyManager {

    private static final String KEY_FILE_NAME = "signingKey.key";



    public static String getSigningKey() {
        String signingKeyPath = System.getenv("KEY_FILE_PATH");

        try {
            Path filePath = Paths.get(signingKeyPath + KEY_FILE_NAME);

            if (Files.exists(filePath)) {
                String existingKey = Files.readString(filePath).trim();

                if (!existingKey.isEmpty()) {
                    return existingKey;
                }

            } else {
                filePath = Paths.get(KEY_FILE_NAME);
            }

            String newKey = generateBase64Key();

            Files.writeString(filePath, newKey, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            return newKey;

        } catch (Exception e) {
            return generateBase64Key();
        }
    }

    private static String generateBase64Key() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSha256");
            keyGen.init(256, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate secure signing key.", e);
        }
    }
}