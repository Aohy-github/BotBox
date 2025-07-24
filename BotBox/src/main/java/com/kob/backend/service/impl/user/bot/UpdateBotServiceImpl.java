package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.updateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class UpdateBotServiceImpl implements updateBotService {


    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> updateBot(Map<String, String> params) {
        Map<String, String> resp = new HashMap<>();

        String botId = params.get("userId");
        String title = params.get("title");
        String description = params.get("description");
        String content = params.get("content");

        LocalDateTime now = LocalDateTime.now();

        Bot newBot = new Bot();
        newBot.setTitle(title);
        newBot.setDescription(description);
        newBot.setId(Integer.parseInt(botId));
        newBot.setModifyTime(now);
        if(botMapper.updateById(newBot) > 0){
            resp.put("error_message", "success");
        }else{
            resp.put("error_message", "update failed");
        }

        return resp;
    }
}
