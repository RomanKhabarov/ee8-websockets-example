package examples.endpoint;

import org.eclipse.jetty.ee8.servlet.ServletContextHandler;
import org.eclipse.jetty.ee8.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import javax.websocket.server.ServerEndpointConfig;

public class EchoServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.start();
        server.join();
    }

    public static Server newServer(int port)
    {
        Server server = new Server(port);

        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);

        JavaxWebSocketServletContainerInitializer.configure(servletContextHandler, (context, container) ->
        {
            ServerEndpointConfig echoConfig = ServerEndpointConfig.Builder.create(EchoServerEndpoint.class, "/echo").build();
            container.addEndpoint(echoConfig);

            ServerEndpointConfig pathParamsConfig = ServerEndpointConfig.Builder.create(EchoServerGenericEndpoint.class, "/test/{param}/category").build();
            container.addEndpoint(pathParamsConfig);
        });

        return server;
    }
}
