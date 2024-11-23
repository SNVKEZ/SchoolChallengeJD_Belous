package org.ifellow.belous;

import com.sun.net.httpserver.HttpServer;
import org.ifellow.belous.handlers.AuthorizationHandler;
import org.ifellow.belous.handlers.OutUserHandler;
import org.ifellow.belous.handlers.RegisterHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/authorization", new AuthorizationHandler());
        server.createContext("/out", new OutUserHandler());
        server.setExecutor(null); // Используется дефолтный пул потоков
        server.start();
        LOGGER.log(Level.INFO, "Server started");
    }
}
