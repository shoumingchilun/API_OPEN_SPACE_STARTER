package com.chilun.apiopenspace.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author 齿轮
 * @date 2024-02-20-16:23
 */
@Configuration
@ConfigurationProperties(prefix = "chilunapispace")
@Data
public class PublicKeyPrivateKeyUtils {
    String privateKey;
    String publicKey;

    public String encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encrypt(data, privateKey);
    }

    public String decrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return decrypt(data, publicKey);
    }

    public boolean verify(String gotData, String wishedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return wishedData.equals(decrypt(gotData));
    }

    public String encrypt(String data, String privateKeyString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 将String类型的私钥转换为privateKey对象
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        // 从密钥规范重建私钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // 创建Cipher对象并初始化为加密模式
        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, privateKey);

        // 加密数据
        byte[] encryptedData = rsa.doFinal(data.getBytes());

        // 将加密后的数据转换为Base64编码的字符串，并返回
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private String decrypt(String encryptedData, String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 将String类型的公钥转换为publicKey对象
        byte[] publicKeySBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeySBytes);

        // 从密钥规范重建私钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // 创建Cipher对象并初始化为解密模式
        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.DECRYPT_MODE, publicKey);

        // 解密数据
        byte[] data = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = rsa.doFinal(data);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
    //    public static void test() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
//        // 使用RSA算法生成KeyPairGenerator对象
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        // 设置密钥长度，一般为1024或2048
//        keyPairGenerator.initialize(2048);
//        // 生成密钥对
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        // 获取公钥和私钥
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        // 打印生成的公钥和私钥
//        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//
//        System.out.println(privateKey.getAlgorithm());
//        System.out.println(privateKey.getFormat());
//        System.out.println(publicKey.getAlgorithm());
//        System.out.println(publicKey.getFormat());
//        System.out.println("publicKey: " + publicKeyString);
//        System.out.println("privateKey: " + privateKeyString);
//
//        String s = "hello world";
//        System.out.println("originalDATA: " + s);
//        String encrypt = new PublicKeyPrivateKeyUtils().encrypt(s, privateKeyString);
//        System.out.println("encryptDATA: " + encrypt);
//        String decrypt = new PublicKeyPrivateKeyUtils().decrypt(encrypt, publicKeyString);
//        System.out.println("decryptDATA: " + decrypt);
//    }
}
