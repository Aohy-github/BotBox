package com.kob.backend.service.user.bot;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface updateBotService {
    Map<String, String> updateBot(@RequestBody Map<String, String> params);
}
