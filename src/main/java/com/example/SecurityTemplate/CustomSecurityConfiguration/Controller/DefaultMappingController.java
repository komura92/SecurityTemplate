package com.example.SecurityTemplate.CustomSecurityConfiguration.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class DefaultMappingController {

    @GetMapping
    public void get() {

    }
}
