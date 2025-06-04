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

    // Mapa from the UUID courier → his last sent position (json-string).
    // You can store Json "hard", which you just need to put the client,
    // or store the Location { double lat, double lng } object and serialize in JSON when sending.
    private final Map<String, String> lastPositions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionManager.addSession(session, "web");
        sessions.add(session);
        System.out.println("The client is connected: " + session.getId());

        //as soon as the new client connected - “ahead of schedule” send him all the last positions
        //(imitating that the couriers had just handed them over)
        for (String lastJson : lastPositions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(lastJson));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String clientMessage = message.getPayload();
            System.out.println("Web Client. Received message: " + clientMessage);

            // First, we will analyze JSON to pull out UUID and coord
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> locationData = mapper.readValue(clientMessage, Map.class);
            String uuid = (String) locationData.get("uuid");
            double lat = (double) locationData.get("lat");
            double lng = (double) locationData.get("lng");

            System.out.println("UUID " + uuid + ", Lat: " + lat + ", Lng: " + lng);

            // We save in lastPositions — in case a new client enters
            lastPositions.put(uuid, clientMessage);

            //ретранслюємо ВСІМ, крім відправника
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(clientMessage));
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
