package com.sanket.chess.auth;

import com.google.api.client.googleapis.apache.v2.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.sanket.chess.mongodb.user.User;
import com.sanket.chess.mongodb.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Component
public class AuthenticationService {

    @Value("${google.clientId}")
    public String clientId;

    private final UserRepository userRepository;

    public AuthenticationService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    User validateToken(String token) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier verifier;
        verifier = new GoogleIdTokenVerifier
                .Builder(GoogleApacheHttpTransport.newTrustedTransport(), Utils.getDefaultJsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
        GoogleIdToken idToken = verifier.verify(token);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            User user = getUser(payload);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null,
                    null, Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
            Optional<User> cUser = userRepository.findByGoogleId(user.getGoogleId());
            if (cUser.isPresent()) {
                user = cUser.get();
            } else {
                user = userRepository.save(user);
            }

            SecurityContextHolder.clearContext();
            return user;
        }

        return null;
    }

    private User getUser(GoogleIdToken.Payload payload) {
        User user = new User();
        user.setGoogleId(payload.getSubject());
        user.setEmail(payload.getEmail());
        user.setEmailVerified(payload.getEmailVerified());
        user.setFullName((String) payload.get("name"));
        user.setPictureUrl((String) payload.get("picture"));
        user.setFirstName((String) payload.get("given_name"));
        user.setLastName((String) payload.get("family_name"));
        user.setLocale((String) payload.get("locale"));
        return user;
    }

}
