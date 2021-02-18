package com.sanket.chess.auth;

import com.sanket.chess.mongodb.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationApi {

    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    public AuthenticationApi(@Autowired AuthenticationService authenticationService,
                             @Autowired JwtTokenService jwtTokenService) {
        this.authenticationService = authenticationService;
        this.jwtTokenService = jwtTokenService;
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public User tokenLogin(@RequestBody Map<String, String> body) throws GeneralSecurityException, IOException {
        User user = authenticationService.validateToken(body.get("id_token"));
        user.setToken(jwtTokenService.generateToken(user));

        return user;
    }

}