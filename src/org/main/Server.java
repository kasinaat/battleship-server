package org.main;

import javax.websocket.*;
import javax.websocket.server.*;

import org.services.*;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/websocket/{username}")
public class Server {
    static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
    static Integer count = 0;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        session.getUserProperties().put("username", username);
        users.add(session);
        System.out.println(session.getUserProperties().get("username"));
        System.out.println("Connection Opened!");
        count++;
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        try {
            // System.out.println(session.getUserProperties().get("username"));
            if (message.equals("READY")) {
                if (users.size() == 2) {
                    for (Session var : users) {
                        var.getBasicRemote().sendText("READY");
                    }
                    GameService.startGame();
                }
                System.out.println("Message from" + session.getUserProperties().get("username") + ": " + message);
                return "WAIT";
            } else if (message.contains("POINT")) {
                String val[] = message.split("#");
                String user = val[2];
                GameService.finishGame(user, Integer.parseInt(val[1]));
            } else {
                for (Session var : users) {
                    if (session.getUserProperties().get("username").equals(var.getUserProperties().get("username")))
                        continue;
                    var.getBasicRemote().sendText(message);
                }
            }
            System.out.println("Message from " + session.getUserProperties().get("username") + " " + message);

        } catch (IOException e) {
            System.out.println("Inside message sender catch");
            try {
                session.getBasicRemote().sendText("LOSER");
                for (Session var : users) {
                    if (session.getUserProperties().get("username").equals(var.getUserProperties().get("username")))
                        continue;
                    var.getBasicRemote().sendText("WINNER_BY_EXIT");
                }
            } catch (IOException ioe) {
                System.out.println("Inside message sender catches catch");
                ioe.printStackTrace();
            }
            users.remove(session);
            count--;
        }
        return null;
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        users.remove(session);

        System.out.println("Connection Closed");
        count--;
    }

    @OnError
    public void onError(Throwable e, Session session) {
        users.remove(session);
        System.out.println("Inside Onerror");
        e.printStackTrace();
        count--;
    }
}