package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.GetUserMessage;
import com.kob.backend.service.user.bot.getBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetBotListServiceImpl implements getBotService {

    @Autowired
    BotMapper botMapper;


    @Override
    public IPage<Bot> getBot(Map<String, String> params) {

        Integer currentPage = Integer.parseInt(params.get("currentPage"));
        Integer pageSize = Integer.parseInt(params.get("pageSize"));
        Integer userId = Integer.parseInt(params.get("userId"));
        IPage<Bot> page = new Page<>(currentPage, pageSize);

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");

        Map<String, String> resp = new HashMap<>();
        IPage<Bot> respPage = botMapper.selectPage(page, queryWrapper);
        respPage.setTotal(botMapper.selectCount(queryWrapper));
        System.out.println(respPage.getRecords().size());  // 获取分页查询结果
        System.out.println(respPage.getTotal());
        return respPage;
    }
}
