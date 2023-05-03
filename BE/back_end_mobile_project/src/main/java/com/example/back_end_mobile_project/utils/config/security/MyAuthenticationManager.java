package com.example.back_end_mobile_project.utils.config.security;

import com.example.back_end_mobile_project.utils.config.security.user.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationManager implements AuthenticationManager {

    private String STRING_WRONG_PASSWORD_INFORMATION = "Wrong password !";

    @Autowired
    MyUserDetailsService service;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UserDetails userDetails = service.loadUserByUsername(authentication.getPrincipal().toString());
        if(!authentication.getCredentials().toString().equals(userDetails.getPassword())){
            throw new BadCredentialsException(STRING_WRONG_PASSWORD_INFORMATION);
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

}
