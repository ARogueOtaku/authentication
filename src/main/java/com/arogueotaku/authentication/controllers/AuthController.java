package com.arogueotaku.authentication.controllers;

import com.arogueotaku.authentication.misc.*;
import com.arogueotaku.authentication.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<HashMap<String, String>>> register(@RequestBody RegisterDetails registerDetails) {
        Response<HashMap<String, String>> response = new Response<>();
        try {
            HashMap<String, String> responseMap = authService.register(registerDetails);
            return ResponseEntity.status(HttpStatus.OK).body(response.data(responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<HashMap<String, String>>> login(@RequestBody LoginDetails loginDetails) {
        Response<HashMap<String, String>> response = new Response<>();
        try {
            HashMap<String, String> responseMap = authService.login(loginDetails);
            return ResponseEntity.status(HttpStatus.OK).body(response.data(responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<HashMap<String, String>>> logout(@RequestBody LogoutDetails logoutDetails) {
        Response<HashMap<String, String>> response = new Response<>();
        try {
            HashMap<String, String> responseMap = authService.logout(logoutDetails);
            return ResponseEntity.status(HttpStatus.OK).body(response.data(responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.error(e.getMessage()));
        }
    }


    @PostMapping("/exchange")
    public ResponseEntity<Response<HashMap<String, String>>> exchangeToken(@RequestBody TokenExchangeDetails tokenExchangeDetails) {
        Response<HashMap<String, String>> response = new Response<>();
        try {
            HashMap<String, String> responseMap = authService.exchangeToken(tokenExchangeDetails);
            return ResponseEntity.status(HttpStatus.OK).body(response.data(responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.error(e.getMessage()));
        }
    }
}
