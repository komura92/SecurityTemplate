package com.example.SimpleSecurity.SimpleSecurity.EmailSender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationLinkSender implements Sender {

    @Override
    public void send(String link, String email) {
        //TODO
        System.out.println("TODO EMAIL SENDER ACTIVATION LINK");
    }
}
