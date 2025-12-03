package com.jok92.workout_tracker_backend.services.auth;

import com.jok92.workout_tracker_backend.config.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtKeyManager.class);

    public static String getSigningKey() {
        String signingKeyPath = System.getenv("KEY_FILE_PATH");
        String fullFilePath = signingKeyPath + "/" + KEY_FILE_NAME;

        logger.debug("Attempting to load/create key at {}", fullFilePath);

        try {
            Path filePath = Paths.get(fullFilePath);

            if (Files.exists(filePath)) {
                logger.info("Signing key file found");
                String existingKey = Files.readString(filePath).trim();

                if (!existingKey.isEmpty()) {
                    logger.info("Signing key read successfully");
                    logger.debug("Signing key: {}", existingKey);
                    return existingKey;
                }

                logger.warn("Signing key file found but was empty. Re-creating key.");
            } else {
                logger.info("Signing key file not found, creating new file and key at {}", fullFilePath);
            }

            String newKey = generateBase64Key();
            logger.debug("Created new key: {}", newKey);

            Files.writeString(filePath, newKey, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            logger.info("New signing key created and written to disk.");
            return newKey;

        } catch (Exception e) {
            logger.error("Error managing signing key file. Generating new temporary key.", e);
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