package com.kob.backend.service.user.bot;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface removeBotService {
    Map<String, String> removeBot(@RequestBody Map<String, String> params);
}
