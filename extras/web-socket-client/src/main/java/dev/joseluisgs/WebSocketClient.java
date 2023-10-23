package dev.joseluisgs;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.container.jdk.client.JdkClientContainer;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {
    private static final String ENDPOINT = "ws://localhost:3000/ws/v1/productos";

    public static void main(String[] args) {
        WebSocketContainer container = ClientManager.createClient(JdkClientContainer.class.getName());
        try {
            Session session = container.connectToServer(WebSocketClient.class, URI.create(ENDPOINT));
            System.out.println("Conectado al servidor WebSocket");

            // Puedes enviar mensajes al servidor usando session.getBasicRemote().sendText("Mensaje");

            // Espera indefinidamente para mantener la conexión abierta
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
    }
}
