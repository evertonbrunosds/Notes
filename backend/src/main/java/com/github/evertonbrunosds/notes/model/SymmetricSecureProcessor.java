package com.github.evertonbrunosds.notes.model;

import static com.github.evertonbrunosds.notes.util.ResourceException.Type.INTERNAL;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.lang.NonNull;

import com.github.evertonbrunosds.notes.util.ResourceException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SymmetricSecureProcessor {

    @NonNull
    private final String algorithm;

    @NonNull
    private final String transformation;

    @NonNull
    private final byte[] key;

    public String encode(@NonNull final String target) {
        try {
            return internalEncode(target);
        } catch (final Throwable throwable) {
            throw new ResourceException(INTERNAL_SERVER_ERROR, INTERNAL);
        }
    }

    public String decode(@NonNull final String target) {
        try {
            return internalDecode(target);
        } catch (final Throwable throwable) {
            throw new ResourceException(INTERNAL_SERVER_ERROR, INTERNAL);
        }
    }

    private String internalDecode(final String target) throws Throwable {
        final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        final byte[] decodedBytes = Base64.getDecoder().decode(target);
        final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private String internalEncode(final String target) throws Throwable {
        final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        final byte[] encryptedBytes = cipher.doFinal(target.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}