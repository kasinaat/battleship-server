package org.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.models.User;
import org.util.DBUtil;

public class UserService {
    public static boolean authenticate(String username, String token) throws SQLException, ClassNotFoundException {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT auth_key from user where username=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.first() == false) {
            return false;
        } else {
            System.out.println(rs.getString(1));
            if (rs.getString(1).equals(token)) {
                System.out.println(rs.getString(1));
                return true;
            }
        }
        return false;
    }

    public static String login(String username, String password) throws SQLException, ClassNotFoundException {
        UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        String authKey = uid.randomUUID().toString();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * from user where username= ? and pass = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return null;
        } else {
            PreparedStatement pq = con.prepareStatement("UPDATE user set auth_key= ? where username= ?");
            pq.setString(1, authKey);
            pq.setString(2, username);
            pq.executeUpdate();
            con.close();
            return authKey;
        }
    }

    public static boolean signup(String username, String password, String email, String firstName, String lastName,
            Long mobile) throws SQLException, ClassNotFoundException {
        UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        String authKey = uid.randomUUID().toString();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT into user values(?,?,?,?,?,?,?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, firstName);
        ps.setString(4, lastName);
        ps.setString(5, email);
        ps.setString(6, authKey);
        ps.setLong(7, mobile);
        if (!ps.execute())
            return true;
        return false;
    }

    public static boolean getEmail(String email) throws SQLException, ClassNotFoundException {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT mail_id from user where mail_id = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.first()) {
            System.out.println("Email Present");
            return true;
        } else {
            return false;
        }
    }

    public static boolean getUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT username from user where username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        System.out.println(rs.toString());
        if (rs.first()) {
            System.out.println("Username Present");
            return true;
        } else {
            return false;
        }
    }

    public static User getUserProfile(String username) throws SQLException, ClassNotFoundException {
        User user = new User();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con
                .prepareStatement("SELECT username,firstname,lastname,mail_id,mobile from user where username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }
        rs.first();
        user.setUsername(rs.getString(1));
        user.setFirstName(rs.getString(2));
        user.setLastName(rs.getString(3));
        user.setEmailId(rs.getString(4));
        user.setMobileNo(rs.getLong(5));
        return user;
    }
}