package com.diffiHellman.client.cryptographyClient.controller;


import com.diffiHellman.client.cryptographyClient.service.DiffieHellmanServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dhc")
public class DiffieHellmanServiceClientController {

    @Autowired
    private  DiffieHellmanServiceClient diffieHellmanServiceClient;



    @PostMapping("/exchange")
    public String keyExchange(){
        diffieHellmanServiceClient.keyExchange(diffieHellmanServiceClient.getPublicKey());
        return "Key exchange is successfully !!!";

    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        diffieHellmanServiceClient.sendMessage(message);
        return "Message sent.";
    }










}
