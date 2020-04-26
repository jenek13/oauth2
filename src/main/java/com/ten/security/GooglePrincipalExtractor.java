package com.ten.security;

import com.ten.model.Role;
import com.ten.model.User;
import com.ten.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.thymeleaf.expression.Sets;

import java.util.Collections;
import java.util.Map;

public class GooglePrincipalExtractor
        //implements PrincipalExtractor
        {

    @Autowired
    private UserService userService;

//    @Override
//    public Object extractPrincipal(Map<String, Object> map) {
//
//        // Check if we've already registered this uer
//        String principalName = map.get("email").toString();
//        User user = userService.getUserByLogin(principalName);
//
//        if (user == null) {
//            // If we haven't registered this user yet, create a new one
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            // This Details object exposes a token that allows us to interact with Facebook on this user's behalf
//            String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
//            user = new User();
//            user.setLogin(principalName);
//            user.setPassword("1234");
//            user.setRoles(Collections.singleton(new Role("ADMIN")));
////                userService.insertUser(user);
//            userService.insertUser(user);
//        }
//        return user;
//    }


}
