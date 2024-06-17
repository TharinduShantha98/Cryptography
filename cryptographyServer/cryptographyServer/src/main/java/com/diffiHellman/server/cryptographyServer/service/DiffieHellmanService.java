package com.diffiHellman.server.cryptographyServer.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class DiffieHellmanService {


    private final BigInteger p = new BigInteger("23");
    private final BigInteger g = new BigInteger("5");

    private final SecureRandom secureRandom = new SecureRandom();

    private BigInteger privateKey;
    @Getter
    private BigInteger publicKey;
    private BigInteger sharedSecret;


    public DiffieHellmanService() {
        this.privateKey = new BigInteger(2048, secureRandom).mod(p);
        this.publicKey = g.modPow(privateKey,p);

        System.out.println("private key :"+privateKey);
        System.out.println("public key :"+publicKey);

    }

    public void computeSharedSecretKey(BigInteger clientPublicKey){
        this.sharedSecret = clientPublicKey.modPow(privateKey,p);
        System.out.println("shared secret key :"+ this.sharedSecret);
    }






}
