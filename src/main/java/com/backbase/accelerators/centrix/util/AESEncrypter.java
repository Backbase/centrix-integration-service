package com.backbase.accelerators.centrix.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AESEncrypter {

    private static final String AES_CYPHER = "AES/CBC/PKCS5PADDING";
    private static final String AES_ALGORITHM = "AES";

    public static String encrypt(String value, String encryptionKey, String initializationVector) {
        try {
            Cipher cipher = initializeCipher(encryptionKey, initializationVector);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            log.warn("Error occurred encrypting value", e);
            return null;
        }
    }

    @SneakyThrows
    private static Cipher initializeCipher(String encryptionKey, String initializationVector) {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_CYPHER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher;
    }
}
