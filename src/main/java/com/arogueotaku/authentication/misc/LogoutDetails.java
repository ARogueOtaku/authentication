package com.arogueotaku.authentication.misc;

import com.fasterxml.jackson.annotation.JsonCreator;

public record LogoutDetails(String refreshToken) {

    @JsonCreator
    public LogoutDetails {
    }

}
