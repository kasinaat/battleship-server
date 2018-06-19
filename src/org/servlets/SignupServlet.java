package org.servlets;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.services.UserService;
import org.util.*;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String check = request.getParameter("check");
        String value = request.getParameter("val");
        
        try {
            if (check.equals("email")) {
                if (UserService.getEmail(value)) {
                    response.setStatus(404);
                } else {
                    response.setStatus(200);
                }
            } else if (check.equals("username")) {
                if (UserService.getUsername(value)) {
                    response.setStatus(404);
                } else {
                    System.out.println("else");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Long mobile = Long.parseLong(request.getParameter("mobile"));
        
        try {
            if(UserService.signup(username,password,email,firstName,lastName,mobile)){
                response.setStatus(200);
            }
            else{
                response.setStatus(400);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch( ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
        
    }

}
