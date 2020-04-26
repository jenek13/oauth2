package com.ten.config;

import com.ten.model.User;
import com.ten.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = (User) userDetailsService.loadUserByUsername(login);

        if(user != null && (user.getUsername().equals(login) || user.getLogin().equals(login)))
        {
            if(!passwordEncoder.matches(password, user.getPassword()))
            {
                throw new BadCredentialsException("Wrong password");
            }

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        }
        else
            throw new BadCredentialsException("Username not found");
    }

    public boolean supports(Class<?> arg)
    {
        return true;
    }
}
