package org.ifellow.belous;

import com.sun.net.httpserver.HttpServer;
import org.ifellow.belous.handlers.song.CreateSongHandler;
import org.ifellow.belous.handlers.user.AuthorizationHandler;
import org.ifellow.belous.handlers.user.OutUserHandler;
import org.ifellow.belous.handlers.user.RegisterHandler;

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
        server.createContext("/song/create", new CreateSongHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.log(Level.INFO, "Server started");
    }
}
