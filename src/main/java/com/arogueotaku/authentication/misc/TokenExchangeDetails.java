package com.arogueotaku.authentication.misc;

import com.fasterxml.jackson.annotation.JsonCreator;

public record TokenExchangeDetails(String refreshToken) {

    @JsonCreator
    public TokenExchangeDetails {
    }

}
