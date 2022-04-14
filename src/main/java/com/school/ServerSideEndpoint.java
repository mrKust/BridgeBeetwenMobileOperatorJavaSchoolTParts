package com.school;

import com.school.aspects.UpdateSender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Server side part of web socket connection
 */
@ServerEndpoint(value = "/tariffsInfo")
@ApplicationScoped
public class ServerSideEndpoint {

    private static final Logger LOG = Logger.getLogger(ServerSideEndpoint.class);

    @Inject
    private SessionRegistry sessionRegistry;

    /**
     * Method connect new user
     * @param session user's session
     * @param conf configuration
     */
    @Interceptors(UpdateSender.class)
    @OnOpen
    public void open(Session session, EndpointConfig conf) {
        sessionRegistry.add(session);
        LOG.setLevel(Level.TRACE);
        LOG.trace("Connected");
    }

    /**
     * Close connection when user disconnect
     * @param session
     * @param reason
     */
    @OnClose
    public void close(Session session, CloseReason reason) {
        sessionRegistry.remove(session);
        LOG.setLevel(Level.TRACE);
        LOG.trace("Disconnected");
    }

    /**
     * Send message to all connected users
     * @param message text of message
     */
    public void send(String message) {
        LOG.setLevel(Level.TRACE);
        LOG.trace("Send " + message);
        sessionRegistry.getAll().forEach(session -> session.getAsyncRemote().sendText(message));
    }

}
