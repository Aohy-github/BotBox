package com.kob.backend.service.impl.user.account;


import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public Map<String, String> info() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        UserDetailsImpl loginUserDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUserDetails.getUser();
        Map<String, String> response = new HashMap<>();

        response.put("error_message" , "success");
        response.put("id", user.getId().toString());
        response.put("username", user.getUsername());
        response.put("photo", user.getPhoto());
        response.put("rating", user.getRating().toString());
        return response;
    }
}
