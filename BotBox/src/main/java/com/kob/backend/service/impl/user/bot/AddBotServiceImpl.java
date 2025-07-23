package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.GetUserMessage;
import com.kob.backend.service.user.bot.addBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddBotServiceImpl implements addBotService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> addBot(Map<String, String> params) {
        Map<String,String> resp = new HashMap<>();

        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");
        String userId = params.get("userId");

        if(title == null || title.equals("")) {
            resp.put("error_message" , "title cannot be empty");
            return resp;
        }

        if(title.length() > 100){
            resp.put("error_message" , "title cannot be longer than 100");
            return resp;
        }


        if(content == null || content.equals("")) {
            resp.put("error_message" , "content cannot be empty");
            return resp;
        }

        if(description == null || description.equals("")) {
            description = "the user too lazy to leave there empty";
        }
        if(description.length() > 100){
            resp.put("error_message" , "description cannot be longer than 100");
            return resp;
        }

        LocalDateTime time = LocalDateTime.now();
        Bot new_bot = new Bot(null , Integer.parseInt(userId) , title , description , content , 1500 , time, time);

        botMapper.insert(new_bot);
        resp.put("error_message" , "success");
        return resp;
    }
}
