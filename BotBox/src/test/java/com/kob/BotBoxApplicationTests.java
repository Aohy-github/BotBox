package com.kob;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.impl.user.bot.AddBotServiceImpl;
import com.kob.backend.service.impl.user.bot.GetBotListServiceImpl;
import com.kob.backend.service.impl.user.bot.RemoveBotServiceImpl;
import com.kob.backend.service.impl.user.bot.UpdateBotServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class BotBoxApplicationTests {

    @Autowired
    private AddBotServiceImpl addBotServiceImpl;
    @Autowired
    private GetBotListServiceImpl getBotListServiceImpl;

    @Autowired
    private RemoveBotServiceImpl removeBotServiceImpl;

    @Autowired
    UpdateBotServiceImpl updateBotServiceImpl;
    @Test
    void contextLoads() {


        Map<String, String> params = new HashMap<>();
        params.put("id", "2");
        params.put("title" , "update title");
        params.put("description" , "update description");

        updateBotServiceImpl.updateBot(params);
    }

}
