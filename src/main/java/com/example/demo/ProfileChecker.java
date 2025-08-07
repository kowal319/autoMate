package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfileChecker implements CommandLineRunner {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void run(String... args) {
        System.out.println("Active Spring profile: " + activeProfile);
    }
}
