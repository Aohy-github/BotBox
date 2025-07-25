package com.kob.backend.controller.user.bot;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.getBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class GetBotListController {
    @Autowired
    getBotService getBotService;


    @GetMapping("/user/bot/getlist/")
    IPage<Bot> getBot(@RequestParam Map<String, String> params){
        // RequestParam 从 url 中匹配数据，明文
        // RequestBody 获取数据，可以反序列化为一个java对象 application/json  application/xml

        for(Map.Entry<String,String> entry : params.entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        return getBotService.getBot(params);
    }
}
