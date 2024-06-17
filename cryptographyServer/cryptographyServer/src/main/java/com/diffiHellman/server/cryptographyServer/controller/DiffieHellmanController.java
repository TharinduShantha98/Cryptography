package com.diffiHellman.server.cryptographyServer.controller;


import com.diffiHellman.server.cryptographyServer.dto.User;
import com.diffiHellman.server.cryptographyServer.service.DiffieHellmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/dis")
public class DiffieHellmanController {

    public final DiffieHellmanService dhService;

    @Autowired
    public DiffieHellmanController(DiffieHellmanService dhService) {
        this.dhService = dhService;
    }

    @PostMapping("/exchange")
    public BigInteger exchangeKeys(@RequestBody BigInteger clientPublicKey) {
        dhService.computeSharedSecretKey(clientPublicKey);
        return dhService.getPublicKey();
    }


    @PostMapping("/message")
    public String sendMessage(@RequestBody String encryptedMessage) {
        System.out.println(encryptedMessage);
        return "Message sent.";
    }





}
