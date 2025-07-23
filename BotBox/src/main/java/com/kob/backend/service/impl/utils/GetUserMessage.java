package com.kob.backend.service.impl.utils;

import com.kob.backend.pojo.User;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class GetUserMessage {
    public User getUser(){
        // 先查询当前登录的用户
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) token.getPrincipal();
        User user = userDetails.getUser();
        return user;
    }
}
