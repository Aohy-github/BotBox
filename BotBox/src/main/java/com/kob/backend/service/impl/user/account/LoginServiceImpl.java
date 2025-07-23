package com.kob.backend.service.impl.user.account;



import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        Map<String, String> response = new HashMap<>();
        if(username == null || password == null) {
            response.put("error_message", "username or password empty");
            return response;
        }

        username = username.trim();
        password = password.trim();
        if(username.length() == 0 || password.length() == 0) {
            response.put("error_message", "username or password empty");
            return response;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 如果登陆失败自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);


        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());


        System.out.println(username + " " + password);
        response.put("error_message", "success");
        response.put("token", jwt);
        System.out.println("token: " + jwt);
        return response;
    }
}
