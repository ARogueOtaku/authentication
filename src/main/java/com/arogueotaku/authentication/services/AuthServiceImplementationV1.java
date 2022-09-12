package com.arogueotaku.authentication.services;

import com.arogueotaku.authentication.misc.LoginDetails;
import com.arogueotaku.authentication.misc.LogoutDetails;
import com.arogueotaku.authentication.misc.RegisterDetails;
import com.arogueotaku.authentication.misc.TokenExchangeDetails;
import com.arogueotaku.authentication.models.Session;
import com.arogueotaku.authentication.models.User;
import com.arogueotaku.authentication.repositories.SessionRepository;
import com.arogueotaku.authentication.repositories.UserRepository;
import com.arogueotaku.authentication.secrets.ServiceAuthProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.*;

@Service
@Primary
@SuppressWarnings("unused")
public class AuthServiceImplementationV1 implements AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ServiceAuthProperties serviceAuthProperties;

    public HashMap<String, String> exchangeToken(TokenExchangeDetails tokenExchangeDetails) throws AuthException, JWTVerificationException, JWTCreationException {
        HashMap<String, String> tokenMap = new HashMap<>();
        String linkId = extractLinkId(tokenExchangeDetails.refreshToken());
        User sessionUser = getUserFromSession(tokenExchangeDetails.refreshToken());
        tokenMap.put("accesstoken", this.generateAccessToken(sessionUser, linkId));
        return tokenMap;
    }

    public HashMap<String, String> register(RegisterDetails registerDetails) throws AuthException {
        HashMap<String, String> userMap = new HashMap<>();
        this.createUser(registerDetails.username(), registerDetails.email(), registerDetails.password());
        userMap.put("username", registerDetails.username());
        userMap.put("email", registerDetails.email());
        return userMap;
    }

    public HashMap<String, String> login(LoginDetails loginDetails) throws AuthException, JWTVerificationException, JWTCreationException {
        HashMap<String, String> loginMap = new HashMap<>();
        User authenticatedUser = this.authenticate(loginDetails.credential(), loginDetails.password());
        Session newSession = this.createSession(authenticatedUser);
        String linkId = this.extractLinkId(newSession.getRefreshToken());
        loginMap.put("accesstoken", this.generateAccessToken(authenticatedUser, linkId));
        loginMap.put("refreshtoken", newSession.getRefreshToken());
        return loginMap;
    }

    public HashMap<String, String> logout(LogoutDetails logoutDetails) throws AuthException {
        HashMap<String, String> logoutMap = new HashMap<>();
        List<Session> deletedSession = sessionRepository.deleteByRefreshToken(logoutDetails.refreshToken());
        if(deletedSession.isEmpty()) throw new AuthException("Invalid Token");
        String linkId = extractLinkId(logoutDetails.refreshToken());
        logoutMap.put("linkid", linkId);
        return logoutMap;
    }

    private User authenticate(String credential, String password) throws AuthException {
        User foundUser = userRepository.findByUsernameOrEmail(credential, credential);
        if(foundUser != null && BCrypt.checkpw(password, foundUser.getPassword())) {
            return foundUser;
        }
        throw new AuthException("Username or Password is Invalid");
    }

    private String generateAccessToken(User user, String linkId) throws JWTCreationException {
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        long tokenDuration = serviceAuthProperties.accesstokenLifetime();
        return JWT.create()
                .withIssuer(serviceAuthProperties.tokenIssuer())
                .withIssuedAt(new Date(currentTime))
                .withExpiresAt(new Date(currentTime + tokenDuration))
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("linkid", linkId)
                .sign(Algorithm.HMAC256(serviceAuthProperties.jwtSecret()));
    }

    private void createUser(String username, String email, String password) throws AuthException {
        try {
            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User(username, email, encryptedPassword);
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new AuthException("Invalid User details");
        }
    }
    private Session createSession(User user) throws JWTCreationException {
        long currentTime = System.currentTimeMillis();
        long tokenDuration = serviceAuthProperties.refreshtokenLifetime();
        Session session = new Session(JWT.create()
                .withIssuer(serviceAuthProperties.tokenIssuer())
                .withIssuedAt(new Date(currentTime))
                .withExpiresAt(new Date(currentTime + tokenDuration))
                .withClaim("linkid", UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(serviceAuthProperties.jwtSecret())), user);
        sessionRepository.save(session);
        return session;
    }

    private DecodedJWT verifyToken(String token) throws AuthException {
        try {
            return JWT.require(Algorithm.HMAC256(serviceAuthProperties.jwtSecret()))
                    .withIssuer(serviceAuthProperties.tokenIssuer())
                    .acceptExpiresAt(10)
                    .build()
                    .verify(token);
        } catch(Exception e) {
            throw new AuthException("Invalid Token");
        }
    }

    private String extractLinkId(String token) throws AuthException {
        DecodedJWT decodedToken = verifyToken(token);
        return decodedToken.getClaim("linkid").asString();
    }

    private User getUserFromSession(String token) throws AuthException {
        try {
            Optional<Session> session = sessionRepository.findByRefreshToken(token);
            if(session.isEmpty()) throw new AuthException();
            Optional<User> user = userRepository.findById(session.get().getUserId());
            if(user.isEmpty()) throw new AuthException();
            return user.get();
        } catch (Exception e) {
            throw new AuthException("Invalid Token");
        }
    }
}
