package com.shengxuan.speed.entity.pojo;

import com.shengxuan.speed.entity.User;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;

public class MyWebSocketSessionAndUser implements Serializable {
    private WebSocketSession session;
    private User user;

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
