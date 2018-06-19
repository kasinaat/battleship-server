package org.models;

import java.sql.Time;

public class Game implements Comparable<Game> {
    private Integer gameId;
    private String player1;
    private String player2;
    private int player1Points;
    private int player2Points;
    private Time timePlayed;

    public Integer getGameId() {
        return this.gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1() {
        return this.player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return this.player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getPlayer1Points() {
        return this.player1Points;
    }

    public void setPlayer1Points(int player1points) {
        this.player1Points = player1points;
    }

    public int getPlayer2Points() {
        return this.player2Points;
    }

    public void setPlayer2Points(int player2points) {
        this.player2Points = player2points;
    }

    public Time getTimePlayed() {
        return this.timePlayed;
    }

    public void setTimePlayed(Time timeplayed) {
        this.timePlayed = timeplayed;
    }
    
    public int compareTo(Game another){
        if(this.gameId > another.gameId){
            return 1;
        }else if(this.gameId < another.gameId){
            return -1;
        } 
        return 0;
    }
}