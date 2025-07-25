package com.kob.backend.controller.user.account;

import com.kob.backend.service.impl.user.account.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@CrossOrigin
public class RegisterController {

    @Autowired
    private RegisterServiceImpl registerService;

    @PostMapping("/user/account/register/")
    public Map<String, String> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String confirmPassword = params.get("confirmPassword");
        return registerService.register(username, password, confirmPassword);
    }
}
