package com.kob.backend.matchingsystem.service.impl;

import com.kob.backend.matchingsystem.service.MatchingService;
import org.springframework.stereotype.Service;


@Service
public class MatchingServiceImpl implements MatchingService {
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("addPlayer" + userId + " " + rating);
        return "addPlayer";
    }

    @Override
    public String removePlayer(Integer userId, Integer rating) {
        System.out.println("removePlayer" + userId + " " + rating);
        return "removePlayer";
    }
}
