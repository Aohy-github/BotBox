package com.kob.backend.consumer;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.Player;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    // private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;
    private Game game = null;
    private static UserMapper userMapper;
    private static RestTemplate restTemplate;

    private final static String addPlayerUrl = "http://127.0.0.1:3032/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3032/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
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
        System.out.println("user: " + user);
        users.put(userId, this);
        System.out.println("connected");
    }

    @OnClose
    public void onClose() {
        session = null;
        if(user != null) {
            users.remove(user.getId());
            // matchPool.remove(user);
        }
        System.out.println("closed");
    }
    private void startGame(Integer aId, Integer bId){
        User userA = userMapper.selectById(aId);
        User userB = userMapper.selectById(bId);


        Integer idA = userA.getId();
        Integer idB = userB.getId();
        Game game = new Game(13,13,20 , idA, idB);
        game.createMap();

        users.get(userA.getId()).game = game;
        users.get(userB.getId()).game = game;

        game.start();
        JSONObject respGame = new JSONObject();
        respGame.put("map", game.getG());
        respGame.put("a_id" , game.getPlayerA().getId());
        respGame.put("a_sx" , game.getPlayerA().getSx());
        respGame.put("a_sy" , game.getPlayerA().getSy());

        respGame.put("b_id" , game.getPlayerB().getId());
        respGame.put("b_sx" , game.getPlayerB().getSx());
        respGame.put("b_sy" , game.getPlayerB().getSy());


        JSONObject respA = new JSONObject();
        respA.put("event", "success");
        respA.put("opponent_username", userB.getUsername());
        respA.put("opponent_photo", userB.getPhoto());
        respA.put("game", respGame);
        System.out.println(respA.toJSONString());
        users.get(userA.getId()).sendMessage(respA.toJSONString());


        JSONObject respB = new JSONObject();
        respB.put("event", "success");
        respB.put("opponent_username", userA.getUsername());
        respB.put("opponent_photo", userA.getPhoto());
        respB.put("game",respGame);
        System.out.println(respB.toJSONString());
        users.get(userB.getId()).sendMessage(respB.toJSONString());
    }
    private void startMatching(){
        System.out.println("startMatching");
//        matchPool.add(this.user);
//
//        while(matchPool.size() >= 2){
//            Iterator<User> iterator = matchPool.iterator();
//            User userA = iterator.next();
//            User userB = iterator.next();
//
//            matchPool.remove(userA);
//            matchPool.remove(userB);
//
//        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", String.valueOf(user.getId()));
        params.add("rating", String.valueOf(user.getRating()));
        restTemplate.postForObject(addPlayerUrl, params, String.class);

    }

    private void stopMatching(){
        System.out.println("stopMatching");
        //matchPool.remove(this.user);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", String.valueOf(user.getId()));
        params.add("rating", String.valueOf(user.getRating()));
        restTemplate.postForObject(removePlayerUrl, params, String.class);
    }
    private void move(int direction){
//        if(game.getPlayerA().getId().equals(this.user.getId())) {
//            System.out.println("playA direction: " + direction);
//            game.setNextStepA(direction);
//        }else if(game.getPlayerB().getId().equals(this.user.getId())) {
//            System.out.println("playB direction: " + direction);
//            game.setNextStepB(direction);
//        }
        if (game.getPlayerA().getId().equals(user.getId())) {
            System.out.println("playA direction: " + direction);
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            System.out.println("playB direction: " + direction);
            game.setNextStepB(direction);
        }

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
        }else if("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessage(String message) {
        System.out.println();
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
