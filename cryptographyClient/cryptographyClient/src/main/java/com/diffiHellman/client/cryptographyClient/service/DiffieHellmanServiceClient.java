package com.diffiHellman.client.cryptographyClient.service;


import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Service
public class DiffieHellmanServiceClient {

    private final BigInteger p = new BigInteger("23");
    private final BigInteger g = new BigInteger("5");

    private final SecureRandom secureRandom = new SecureRandom();


    @Getter
    private BigInteger publicKey;
    private BigInteger privateKey;

    private BigInteger sharedSecret;


    public DiffieHellmanServiceClient() {
        this.privateKey = new BigInteger(2048,secureRandom).mod(p);
        this.publicKey = g.modPow(privateKey,p);
        System.out.println("private key :"+ privateKey);
        System.out.println("public key :"+ publicKey);

    }


    public BigInteger computeSharedSecretKey(BigInteger serverPublicKey){
        return serverPublicKey.modPow(privateKey, p);

    }

    public void keyExchange(BigInteger clientPublicKey){
        RestTemplate restTemplate = new RestTemplate();
        BigInteger serverPublicKey = restTemplate.postForObject("http://localhost:8081/api/dis/exchange", publicKey, BigInteger.class);
        assert serverPublicKey != null;
        this.sharedSecret =  computeSharedSecretKey(serverPublicKey);
        System.out.println("shared secret key : "+ this.sharedSecret);
    }



    private SecretKeySpec keySpec(){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = sha.digest(sharedSecret.toByteArray());
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String encryptMessage(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = keySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));

    }



    public void sendMessage(String message){

        RestTemplate restTemplate = new RestTemplate();
        try {
            String encryptedMessage = encryptMessage(message);
            restTemplate.postForObject("http://localhost:8081/api/dis/message", encryptedMessage, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
