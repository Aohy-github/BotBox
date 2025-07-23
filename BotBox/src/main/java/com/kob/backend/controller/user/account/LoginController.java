package com.kob.backend.controller.user.account;


import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/user/account/token/")
    public Map<String , String> getToken(@RequestBody Map<String, String> params) {
        System.out.println("account/token");
        String username = params.get("username");
        String password = params.get("password");

        System.out.println(username + " " + password);
        return loginService.getToken(username, password);
    }

}
