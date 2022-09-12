package com.arogueotaku.authentication.services;

import com.arogueotaku.authentication.misc.LogoutDetails;
import com.arogueotaku.authentication.misc.RegisterDetails;
import com.arogueotaku.authentication.misc.TokenExchangeDetails;
import com.arogueotaku.authentication.misc.LoginDetails;

import javax.security.auth.message.AuthException;
import java.util.*;

public interface AuthService {
    HashMap<String, String> exchangeToken(TokenExchangeDetails tokenExchangeDetails) throws AuthException;

    HashMap<String, String> register(RegisterDetails registerDetails) throws AuthException;

    HashMap<String, String> login(LoginDetails loginDetails) throws AuthException;

    HashMap<String, String> logout(LogoutDetails logoutDetails) throws AuthException;
}
