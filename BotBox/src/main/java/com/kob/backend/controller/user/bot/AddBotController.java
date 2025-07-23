package com.kob.backend.controller.user.bot;


import com.kob.backend.service.impl.user.bot.AddBotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class AddBotController {

    @Autowired
    private AddBotServiceImpl botService;

    @PostMapping("/user/bot/add/")
    Map<String , String> addBot(@RequestBody Map<String,String> params){
        for(Map.Entry<String,String> entry : params.entrySet()){
            System.out.println(entry.getKey());
        }
        return botService.addBot(params);
    }

}
