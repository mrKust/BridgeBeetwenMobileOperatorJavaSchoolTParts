package com.school;

import com.school.aspects.UpdateSender;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/tariffsInfo")
@ApplicationScoped
public class ServerSideEndpoint {

    @Inject
    private SessionRegistry sessionRegistry;

    @Interceptors(UpdateSender.class)
    @OnOpen
    public void open(Session session, EndpointConfig conf) {
        sessionRegistry.add(session);
        System.out.println("Connected");
    }

    @OnClose
    public void close(Session session, CloseReason reason) {
        sessionRegistry.remove(session);
        System.out.println("Disconnected");
    }

    public void send(String message) {
        System.out.println("Send " + message);
        sessionRegistry.getAll().forEach(session -> session.getAsyncRemote().sendText(message));
    }

}
