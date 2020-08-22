package com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationLinkSender implements Sender {

    @Override
    public void send(String link, String email) {
        //TODO
        System.out.println("TODO EMAIL SENDER ACTIVATION LINK");
    }
}
