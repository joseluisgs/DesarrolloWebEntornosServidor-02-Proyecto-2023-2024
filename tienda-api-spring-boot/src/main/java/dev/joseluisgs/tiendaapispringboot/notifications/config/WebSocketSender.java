package dev.joseluisgs.tiendaapispringboot.notifications.config;

import java.io.IOException;

/**
 * Interfaz para enviar mensajes por WebSockets
 */
public interface WebSocketSender {

    void sendMessage(String message) throws IOException;

    void sendPeriodicMessages() throws IOException;
}