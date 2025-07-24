package com.kob.backend.consumer.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;


public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer walls_count;
    final private int[][] g;

    final private static int[] dx = {-1 ,0 ,1 ,0}, dy = {0 ,1 , 0 , -1};


    public Game(Integer rows, Integer cols, Integer walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.walls_count = walls_count;
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
}
