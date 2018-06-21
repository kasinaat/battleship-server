package org.servlets;

import java.io.*;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.models.Game;
import org.models.GameRoom;
import org.models.User;
import org.services.GameService;
import org.services.UserService;
import org.util.*;
import com.google.gson.Gson;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        try {
            if (query.equals("profile")) {
                String username = request.getParameter("user");
                User user = UserService.getUserProfile(username);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.write(new Gson().toJson(user));
                pw.close();
            } else if (query.equals("history")) {
                String username = request.getParameter("user");
                Set<Game> games = GameService.getGameHistory(username);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.write(new Gson().toJson(games));
                pw.close();
            } else if (query.equals("games")) {
                String gameType = request.getParameter("type");
                if (gameType.equals("new")) {
                    Set<GameRoom> games = GameService.getNewGames();
                    PrintWriter pw = response.getWriter();
                    pw.write(new Gson().toJson(games));
                    pw.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        Integer gameId = GameService.startGame(username);
        String res = "{\"gameId\":"+gameId+"}";
        PrintWriter pw = response.getWriter();
        pw.write(res);
        pw.close();
    }
}