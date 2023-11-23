package com.user.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${password.pattern}")
    private String passwordPattern;

    public String getPasswordPattern() {
        return passwordPattern;
    }
}
