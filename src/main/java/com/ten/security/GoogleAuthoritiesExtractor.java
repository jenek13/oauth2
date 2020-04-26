package com.ten.security;

import com.ten.model.User;
import com.ten.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GoogleAuthoritiesExtractor implements AuthoritiesExtractor {


    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        return null;
    }
}
