package com.kob.backend.service.user.bot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.pojo.Bot;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface getBotService {
    IPage<Bot> getBot(@RequestBody Map<String, String> params);
}
