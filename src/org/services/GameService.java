package org.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.models.Game;
import org.util.DBUtil;

public class GameService {
    public static void startGame() {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT into game(time_played) values(NOW())");
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        

    }

    public static void finishGame(String user, int point) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "select auto_increment from information_schema.tables where table_schema = 'battleship' and table_name = 'game'");
            ResultSet rs = ps.executeQuery();
            rs.first();
            int gameId = rs.getInt(1);
            System.out.println("Finishing Game");
            ps = con.prepareStatement("INSERT into player_game values(?,?,?)");
            ps.setString(1, user);
            ps.setInt(2, gameId - 1);
            ps.setInt(3, point);
            ps.execute();
            System.out.println("Finishing Game");
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

    }
    public static Set<Game> getGameHistory(String username){
        Set<Game> games = new TreeSet<Game>();
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "select * from player_game where username = ?");
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            Game game = null;
            
            while(rs.next()){
                game = new Game();
                game.setPlayer1(username);
                game.setPlayer1Points(rs.getInt(3));
                ps = con.prepareStatement("Select username,score from player_game where game_id = ? and username != ?");
                ps.setInt(1, rs.getInt(2));
                ps.setString(2, username);
                game.setGameId(rs.getInt(2));
                ResultSet rn = ps.executeQuery();
                while(rn.next()){
                    game.setPlayer2(rn.getString(1));
                    game.setPlayer2Points(rn.getInt(2));
                }
                games.add(game);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return games;
    }
}