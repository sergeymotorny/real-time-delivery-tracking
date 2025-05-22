package com.motorny.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandlerForWebClients extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionManager.addSession(session, "web");
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String clientMessage = message.getPayload();
            System.out.println("Web Client = Received message: " + clientMessage);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> locationData = mapper.readValue(clientMessage, Map.class);

            String uuid = (String) locationData.get("uuid");
            double lat = (double) locationData.get("lat");
            double lng = (double) locationData.get("lng");

            System.out.println("UUID " + uuid + ", Lat: " + lat + ", Lng: " + lng);

            for (WebSocketSession s : sessions) {
                if (s.isOpen() && s != session) {
                    s.sendMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionManager.removeSession(session, "web");
        sessions.remove(session);
    }
}
