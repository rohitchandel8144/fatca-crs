package com.rcs.regulatoryComplianceSystem.util;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {

    public static PrivateKey loadPrivateKey(String resourcePath) throws Exception {
        try (InputStream inputStream = KeyLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Private key file not found: " + resourcePath);
            }

            // Read file content and clean it
            String keyContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----BEGIN.*?-----", "")
                    .replaceAll("-----END.*?-----", "")
                    .replaceAll("\\s+", ""); // Remove newlines and spaces

            byte[] keyBytes = Base64.getDecoder().decode(keyContent);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    public static PublicKey loadPublicKey(String certPath) throws Exception {
        try (InputStream inputStream = KeyLoader.class.getClassLoader().getResourceAsStream(certPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Certificate file not found in classpath: " + certPath);
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            return cert.getPublicKey();
        }
    }
}
