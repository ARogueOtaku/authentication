package com.arogueotaku.authentication.misc;

import com.fasterxml.jackson.annotation.JsonCreator;

public record LoginDetails(String credential, String password) {

    @JsonCreator
    public LoginDetails {
    }
}
