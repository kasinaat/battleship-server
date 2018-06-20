package org.main;

import javax.websocket.*;
import javax.websocket.server.*;

import org.services.*;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/websocket/{username}/{id}")
public class Server {
    static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
    static HashMap<Integer,String> games = new HashMap<Integer,String>();
    

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username,@PathParam("id") Integer gameId) {
        session.getUserProperties().put("username", username);
        session.getUserProperties().put("gameId", gameId);
        users.add(session);
        try {
            if(games.containsKey(gameId)){
                games.put(gameId, "PLAYING");
                for (Session var : users) { 
                    if(var.getUserProperties().get("gameId") == gameId && var.getUserProperties().get("username") != username){
                        var.getBasicRemote().sendText("READY#"+session.getUserProperties().get("username"));
                        session.getBasicRemote().sendText("READY#"+var.getUserProperties().get("username"));
                        GameService.startGame(username);
                        break;
                    }
                }
            session.getBasicRemote().sendText("WAIT");
            } else{
                games.put(gameId, "NEW");
                session.getBasicRemote().sendText("WAIT");
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        try {
            // System.out.println(session.getUserProperties().get("username"));
            if (message.contains("POINT")) {
                String val[] = message.split("#");
                String user = val[2];
                GameService.finishGame(user, Integer.parseInt(val[1]));
                Integer gameId = Integer.parseInt(session.getUserProperties().get("gameId").toString());
                games.put(gameId, "COMPLETED");
            } else {
                String[] values = message.split("#");
                for (Session var : users) {
                    if (var.getUserProperties().get("username").equals(values[2])){
                        var.getBasicRemote().sendText(message);
                        break;
                    }
                }
            }
            System.out.println("Message from " + session.getUserProperties().get("username") + " " + message);

        } catch (IOException e) {
            games.put(Integer.parseInt(session.getUserProperties().get("gameId").toString()), "COMPLETED");
            users.remove(session);
        }
        return null;
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        games.put(Integer.parseInt(session.getUserProperties().get("gameId").toString()), "COMPLETED");
        users.remove(session);
        System.out.println("Connection Closed");
    }

    @OnError
    public void onError(Throwable e, Session session) {
        users.remove(session);
        games.put(Integer.parseInt(session.getUserProperties().get("gameId").toString()), "COMPLETED");
        System.out.println("Inside Onerror");
        e.printStackTrace();
    }
}