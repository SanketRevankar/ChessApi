package com.sanket.chess.config;

import com.sanket.chess.auth.SecurityServletFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityServletFilter securityServletFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .authorizeRequests().antMatchers("/api/login").permitAll()
                .and().authorizeRequests().antMatchers("/ws/**", "/ws/**/**").permitAll()
                .and().authorizeRequests().antMatchers("/api/user/{userId}")
                .access("@webSecurity.checkUserHasAccessToUserId(authentication, #userId)")
                .and().authorizeRequests().anyRequest().authenticated()
                .and().logout().permitAll()
                .and().addFilterBefore(securityServletFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
