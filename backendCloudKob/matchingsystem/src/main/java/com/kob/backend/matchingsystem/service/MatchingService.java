package com.kob.backend.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId , Integer rating);

    String removePlayer(Integer userId , Integer rating);
}
