package com.rcs.regulatoryComplianceSystem.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class FatcaXmlEncryptor {
    public static String encryptXml(String signedXml, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedData = cipher.doFinal(signedXml.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
}