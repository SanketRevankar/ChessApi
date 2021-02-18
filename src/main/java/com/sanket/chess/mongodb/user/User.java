package com.sanket.chess.mongodb.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;
    private String googleId;
    private String email;
    private Boolean emailVerified;
    private String fullName;
    private String pictureUrl;
    private String firstName;
    private String lastName;
    private String locale;
    private String token;

    public User (String id, String name) {
        this.id = id;
        fullName = name;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore @Override
    public String getPassword() {
        return null;
    }

    @JsonIgnore @Override
    public String getUsername() {
        return fullName;
    }

    @JsonIgnore @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isEnabled() {
        return true;
    }
}
