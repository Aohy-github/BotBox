package com.kob.backend.consumer;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.config.WebSocketConfig;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.SocketJwtAuthentication;
import com.kob.backend.consumer.utils.SpringConfigurator;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
// 这里的Websocket会是一个多例，而不是单例，需要手动开启多例模式
@Scope("prototype")
@ServerEndpoint(
        value = "/websocket/{token}",
        configurator = SpringConfigurator.class
        // SpringConfigurator 是为了解决 UserMapper 为null
)
public class WebSocketServer {

    // 建立一个公共的static 线程安全表，维护每个用户的 socket 链接
    private static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;

    private static UserMapper userMapper;


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        System.out.println("token: " + token);
        this.session = session;
        Integer userId = SocketJwtAuthentication.getUserId(token);
        System.out.println("userId: " + userId);
        if (userId == null) {
            // token 无效，拒绝连接
            System.out.println("Invalid token, closing session");
            session.close();
            return;
        }
        // 合法，继续后续处理
        this.user = userMapper.selectById(userId);
        users.put(userId, this);
        System.out.println("connected");
    }

    @OnClose
    public void onClose() {
        this.session = null;
        if(this.user != null) {
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
        System.out.println("closed");
    }

    private void startMatching(){
        System.out.println("startMatching");
        matchPool.add(this.user);

        while(matchPool.size() >= 2){
            Iterator<User> iterator = matchPool.iterator();
            User userA = iterator.next();
            User userB = iterator.next();

            matchPool.remove(userA);
            matchPool.remove(userB);

            Game game = new Game(13,13,20);
            game.createMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "success");
            respA.put("opponent_username", userB.getUsername());
            respA.put("opponent_photo", userB.getPhoto());
            respA.put("gamemap", game.getG());
            System.out.println(respA.toJSONString());
            users.get(userA.getId()).sendMessage(respA.toJSONString());


            JSONObject respB = new JSONObject();
            respB.put("event", "success");
            respB.put("opponent_username", userA.getUsername());
            respB.put("opponent_photo", userA.getPhoto());
            respB.put("gamemap", game.getG());
            System.out.println(respB.toJSONString());
            users.get(userB.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void stopMatching(){
        System.out.println("stopMatching");
        matchPool.remove(this.user);
    }

    // 负责接收前端的send 内容
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("received message: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            startMatching();
        }else if("stop-matching".equals(event)) {
            stopMatching();
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    private void sendMessage(String message) {
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
