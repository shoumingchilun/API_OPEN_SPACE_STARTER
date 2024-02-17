package com.chilun.apiopenspace.starter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 齿轮
 * @date 2024-02-17-14:57
 */
public class SignatureUtils {
    public static String generateSignature(String accesskey, String secretkey, String GatewayAPIURI, long beginStamp, Integer salt) throws NoSuchAlgorithmException {
        return SignatureUtils.generateSignature(accesskey + secretkey + GatewayAPIURI + beginStamp + salt, secretkey);
    }

    public static String generateSignature(String data, String secretkey) throws NoSuchAlgorithmException {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA-256");
        SecretKeySpec secretKey = new SecretKeySpec(secretkey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        digest.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] hash = digest.digest(secretKey.getEncoded());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean verifySignature(String accesskey, String secretkey, String GatewayAPIURI, long beginStamp, Integer salt, String sign) throws NoSuchAlgorithmException {
        return SignatureUtils.verifySignature(accesskey + secretkey + GatewayAPIURI + beginStamp + salt, secretkey, sign);
    }

    public static boolean verifySignature(String data, String secretkey, String sign) throws NoSuchAlgorithmException {
        return generateSignature(data, secretkey).equals(sign);
    }
}
