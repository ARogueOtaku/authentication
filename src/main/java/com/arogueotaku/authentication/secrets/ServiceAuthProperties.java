package com.arogueotaku.authentication.secrets;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service.auth")
public record ServiceAuthProperties(String jwtSecret, String tokenIssuer, long refreshtokenLifetime, long accesstokenLifetime) {
}
