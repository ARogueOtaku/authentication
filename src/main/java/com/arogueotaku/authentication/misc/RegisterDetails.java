package com.arogueotaku.authentication.misc;

import com.fasterxml.jackson.annotation.JsonCreator;

public record RegisterDetails(String username, String email, String password) {

    @JsonCreator
    public RegisterDetails {
    }
}
