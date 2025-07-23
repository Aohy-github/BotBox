package com.kob.backend.service.user.bot;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface addBotService {
    Map<String, String> addBot(@RequestBody Map<String, String> params);
}
