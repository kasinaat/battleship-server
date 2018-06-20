package org.models;

public class GameRoom {
    private Integer gameId;
    private String gameStatus;
    private String creator;

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public String getGameStatus() {
        return this.gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}