package com.kob.backend.controller.user.account;


import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class InfoController {
    @Autowired
    private InfoService infoService;
    @GetMapping("/user/account/info/")
    public Map<String , String> getInfo(@RequestParam Map<String, String> params) {
        System.out.println("account/info");
//        for(Map.Entry<String, String> entry : params.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
//        System.out.println(params);

        return infoService.info();
    }

}
