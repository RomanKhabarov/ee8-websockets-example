package examples.endpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class EchoTest {
    private Server server;
    private List<String> messages;
    private ClientSocketEndpoint clientEndPoint;

    @BeforeEach
    public void startServerAndClient() throws Exception {
        messages = new ArrayList<>();
        server = EchoServer.newServer(0);
        server.start();

        clientEndPoint = new ClientSocketEndpoint();
        clientEndPoint.addMessageHandler(message -> {
            System.out.println(message);
            messages.add(message);
        });
    }

    @AfterEach
    public void stopAll() {
        LifeCycle.stop(server);
        messages.clear();
    }

    @Test
    public void testGenericEcho() throws Exception {
        URI uri = new URI("ws", server.getURI().getAuthority(), "/test/p1/category", null, null);
        clientEndPoint.connect(uri);
        clientEndPoint.close();

        assertThat(messages, contains("You are now connected to " + EchoServerGenericEndpoint.class.getName() + " p1"));
    }

    @Test
    public void testEcho() throws Exception {
        URI uri = new URI("ws", server.getURI().getAuthority(), "/echo", null, null);
        clientEndPoint.connect(uri);
        clientEndPoint.close();

        assertThat(messages, contains("You are now connected to " + EchoServerEndpoint.class.getName()));
    }
}
