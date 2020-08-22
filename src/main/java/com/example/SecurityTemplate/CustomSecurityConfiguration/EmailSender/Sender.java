package com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender;

public interface Sender {
    void send(String link, String email);
}
