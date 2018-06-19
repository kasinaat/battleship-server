package org.servlets;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.services.UserService;
import org.util.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        String token = request.getParameter("token");
        if(username == null || token == null){
            response.setStatus(403);
        } else{
            try {
                if(UserService.authenticate(username,token)){
                    response.setStatus(200);
                } else{
                    response.setStatus(403);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch(ClassNotFoundException cnfe){
                cnfe.printStackTrace();
            }
            
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            String token = UserService.login(username,password);
            if( token != null){
                response.setStatus(200);
                PrintWriter pw = response.getWriter();
                // System.out.println(token);
                pw.write(token);
                pw.close();
            } else{
                response.setStatus(403);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
        
    }
}