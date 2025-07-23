package com.kob.backend.controller.pk;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {


//    @RequestMapping("/pk/getbotinfo/")
    public Map<String,String> getBotInfo(){
        Map<String,String> bo11 = new HashMap<>();
         bo11.put("name" , "apple");
         return bo11;
    }
}
