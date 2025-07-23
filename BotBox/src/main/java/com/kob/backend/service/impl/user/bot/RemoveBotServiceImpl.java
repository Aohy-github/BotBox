package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.GetUserMessage;
import com.kob.backend.service.user.bot.removeBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class RemoveBotServiceImpl implements removeBotService {


    @Autowired
    private BotMapper botMapper;


    @Autowired
    GetUserMessage getUserMessage;
    @Override
    public Map<String, String> removeBot(Map<String, String> params) {
        Map<String, String> resp = new HashMap<>();
        Integer id = Integer.parseInt(params.get("id"));

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);
        if(botMapper.delete(queryWrapper) > 0){
            resp.put("error_message" , "success");
        }else{
            resp.put("error_message" , "remove bot failed");
        }

        return resp;
    }
}
