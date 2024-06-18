package ai.cheshirecat.logic;

import ai.cheshirecat.exceptions.WaitingResponseException;
import ai.cheshirecat.models.UserMessage;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

public class CatClient extends WebSocketClient {

    private Consumer<String> onMessageReceived;
    private Consumer<Exception> onError;
    private Consumer<ServerHandshake> onOpen;
    private Consumer<String> onClose;

    private final Gson jsonSerializer;

    private boolean waitingForResponse;

    private String userId;

    public CatClient(String catIpAddress, int catPort, String userId) {
        super(URI.create("ws://" + catIpAddress + ":" + catPort + "/ws/" + userId));

        this.userId = userId;

        this.waitingForResponse = false;

        this.jsonSerializer = new Gson();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        if (this.onOpen != null) {
            this.onOpen.accept(serverHandshake);
            return;
        }

        System.out.println("CAT Connection opened");
    }

    public void onOpen(Consumer<ServerHandshake> onOpen) {
        this.onOpen = onOpen;
    }

    @Override
    public void onMessage(String s) {
        this.waitingForResponse = false;
        if(onMessageReceived != null) {
            onMessageReceived.accept(s);
            return;
        }
        System.out.println(s);
    }

    public void onMessage(Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        if(this.onClose != null) {
            this.onClose.accept(s);
            return;
        }

        System.out.println("WebSocket Connection closed");
    }

    @Override
    public void onError(Exception e) {
        if(onError != null) {
            this.onError.accept(e);
            return;
        }
        System.out.println("Got Error: ");
        System.out.println(e.getMessage());
    }

    public void onError(Consumer<Exception> onError) {
        this.onError = onError;
    }

    // Invia un messaggio con userId impostato
    public void sendMessage(String message) {
        this.send(message);
    }

    @Override
    public void send(String message) {
        if (!this.isOpen()) {
            throw new RuntimeException("Trying to send a message without an active WebSocket connection");
        }
        var userMessage = new UserMessage(message, this.userId);
        this.send(userMessage);
    }

    public void send(UserMessage message) {
        if(this.waitingForResponse) {
            throw new WaitingResponseException();
        }
        if (!this.isOpen()) {
            throw new RuntimeException("Trying to send a message without an active WebSocket connection");
        }
        var json = this.jsonSerializer.toJson(message);
        super.send(json);
    }


}
