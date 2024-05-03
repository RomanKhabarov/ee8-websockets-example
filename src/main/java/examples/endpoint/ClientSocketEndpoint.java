package examples.endpoint;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

@ClientEndpoint
public class ClientSocketEndpoint {

    private Session userSession = null;
    private MessageHandler messageHandler;
    private WebSocketContainer container;

    public ClientSocketEndpoint() {
        container = ContainerProvider.getWebSocketContainer();
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnMessage
    public void onMessage(ByteBuffer bytes) {
        System.out.println("Handle byte buffer");
    }

    public void connect(URI endpointURI) {
        try {
            container.connectToServer(this, endpointURI);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            userSession.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public interface MessageHandler {

        void handleMessage(String message);
    }
}