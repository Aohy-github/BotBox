package com.kob.backend.consumer.utils;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class Game extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer walls_count;
    final private int[][] g;

    final private static int[] dx = {-1 ,0 ,1 ,0}, dy = {0 ,1 , 0 , -1};
    private final Player playerA ,  playerB;
    // 游戏状态
    private String status = "playing";  // playing finished
    private String loser = "";  // all: 平局 A, B
    // 0 1 2 3   上 下 左 右
    private Integer nextStepA = null;
    private Integer nextStepB = null;


    private ReentrantLock lock = new ReentrantLock();

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally{
            lock.unlock();
        }

    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }



    public Game(Integer rows, Integer cols, Integer walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.walls_count = walls_count;
        this.playerA = new Player(idA , this.rows - 2, 1 , new ArrayList<>());
        this.playerB = new Player(idB , 1, this.cols - 2 , new ArrayList<>());
        this.g = new int[rows][cols];
    }

    private boolean check_connectivity(int sx, int sy ,int tx , int ty) {
        if(sx == tx && sy == ty) return true;
        this.g[sx][sy] = 1;

        for(int i = 0 ; i < 4; i++){
            int x = sx + dx[i];
            int y = sy + dy[i];
            if(x >= 0 &&  x <= this.rows - 1 && y >= 0 && y <= this.cols - 1 && g[x][y] == 0) {
                if(check_connectivity(x,y,tx,ty)){
                    this.g[sx][sy] = 0;
                    return true;
                }
            }
        }

        this.g[sx][sy] = 0;
        return false;
    }
    private boolean draw(){
        for(int i = 0 ;i < rows; i++){
            for(int j = 0; j < cols; j++){
                g[i][j] = 0;
            }
        }

        for(int i = 0 ;i < rows; i++){
            g[i][0] = g[i][this.cols-1] = 1;
        }

        for(int i = 0 ;i < cols; i++){
            g[0][i] = g[this.rows - 1][i] = 1;
        }
        Random random = new Random();
        for(int i = 0; i < this.walls_count / 2; i++){
            for(int j = 0; j < 1000 ; j++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if(g[r][c] == 1 || g[this.rows -1 - r][this.cols - 1- c] == 1){
                    continue;
                }

                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }
                g[r][c] = 1;
                g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1 ,1, this.cols - 2);
    }

    public int[][] getG(){
        return this.g;
    }

    public void createMap(){
        for(int i = 0 ; i < 1000 ;i++){
            if(draw())
                break;
        }
    }

    // 等待玩家的下一步操作
    private boolean nextStep(){
        try{
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0 ; i < 50 ;  i++){
            try{
                Thread.sleep(100);
                lock.lock();
                try{
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e ){
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendMove () {
        // 发送两个玩家的操作
        this.lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);

            sendMessageToALl(resp.toJSONString());
            // 清空
            nextStepA = nextStepB = null;
        }
        finally {
            this.lock.unlock();
        }
    }

    public void sendMessageToALl (String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendResult(){// 公布结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser" , loser);
        sendMessageToALl(resp.toJSONString());
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }

        return true;
    }

    private void judge() {  // 判断两名玩家下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    @Override
    public void run() {
        for(int i =0 ; i < 100; i ++){
            if(nextStep()){
                judge();
                if(status.equals("playing")){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                status = "finished";
                lock.lock();
                try{
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }else if(nextStepA == null){
                        loser = "A";
                    }else {
                        loser = "B";
                    }
                }finally{
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
