package examples.endpoint;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;

public class EchoServerEndpoint extends Endpoint {
    private static final Logger LOG = LoggerFactory.getLogger(EchoServerEndpoint.class);

    @Override
    public void onClose(Session session, CloseReason close)  {
        super.onClose(session, close);
        LOG.info("WebSocket Close: {} - {}", close.getCloseCode(), close.getReasonPhrase());
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        LOG.info("WebSocket Open: {}", session);
        session.getAsyncRemote().sendText("You are now connected to " + this.getClass().getName());
    }

    @Override
    public void onError(Session session, Throwable cause) {
        super.onError(session, cause);
        LOG.warn("WebSocket Error", cause);
    }
}
