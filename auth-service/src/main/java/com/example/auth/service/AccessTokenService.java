package com.example.auth.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class AccessTokenService {
    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secret;
    private final long ttlSeconds;

    public AccessTokenService(String secret, long ttlSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.ttlSeconds = ttlSeconds;
    }

    public String createToken(String username) {
        long issuedAt = Instant.now().getEpochSecond();
        long expiresAt = issuedAt + ttlSeconds;
        String payload = "sub=" + encode(username) + ";iat=" + issuedAt + ";exp=" + expiresAt;
        String signingInput = base64Url(HEADER) + "." + base64Url(payload);
        return signingInput + "." + base64Url(sign(signingInput));
    }

    public boolean isValid(String token) {
        return extractUsername(token) != null;
    }

    public String extractUsername(String token) {
        ParsedToken parsedToken = parse(token);
        if (parsedToken == null) {
            return null;
        }

        long expiresAt = parseLong(parsedToken.exp);
        if (expiresAt <= Instant.now().getEpochSecond()) {
            return null;
        }

        return decode(parsedToken.sub);
    }

    private ParsedToken parse(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return null;
        }

        String signingInput = parts[0] + "." + parts[1];
        byte[] expectedSignature = sign(signingInput);
        byte[] providedSignature;

        try {
            providedSignature = Base64.getUrlDecoder().decode(parts[2]);
        } catch (IllegalArgumentException exception) {
            return null;
        }

        if (expectedSignature.length != providedSignature.length || !constantTimeEquals(expectedSignature, providedSignature)) {
            return null;
        }

        String payload;
        try {
            payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException exception) {
            return null;
        }

        ParsedToken parsedToken = new ParsedToken();
        for (String entry : payload.split(";")) {
            String[] keyValue = entry.split("=", 2);
            if (keyValue.length != 2) {
                return null;
            }

            switch (keyValue[0]) {
                case "sub" -> parsedToken.sub = keyValue[1];
                case "iat" -> parsedToken.iat = keyValue[1];
                case "exp" -> parsedToken.exp = keyValue[1];
                default -> {
                }
            }
        }

        return parsedToken.sub != null && parsedToken.exp != null ? parsedToken : null;
    }

    private byte[] sign(String signingInput) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign access token", exception);
        }
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception exception) {
            return -1L;
        }
    }

    private boolean constantTimeEquals(byte[] expected, byte[] provided) {
        int result = 0;
        for (int index = 0; index < expected.length; index++) {
            result |= expected[index] ^ provided[index];
        }
        return result == 0;
    }

    private static class ParsedToken {
        private String sub;
        private String iat;
        private String exp;
    }
}