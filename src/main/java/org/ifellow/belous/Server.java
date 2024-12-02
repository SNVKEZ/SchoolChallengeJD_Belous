package org.ifellow.belous;

import com.sun.net.httpserver.HttpServer;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.handlers.comment.CreateCommentHandler;
import org.ifellow.belous.handlers.song.CreateSongHandler;
import org.ifellow.belous.handlers.song.GetConcertHandler;
import org.ifellow.belous.handlers.song.GradeSongHandler;
import org.ifellow.belous.handlers.user.AuthorizationHandler;
import org.ifellow.belous.handlers.user.DeleteUserHandler;
import org.ifellow.belous.handlers.user.OutUserHandler;
import org.ifellow.belous.handlers.user.RegisterHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //слушаем когда поток глохнет, тогда записываем данные из бд в файл
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Database.saveToFile("src/main/resources/database.dat");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        Database.loadFromFile("src/main/resources/database.dat");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/authorization", new AuthorizationHandler());
        server.createContext("/out", new OutUserHandler());
        server.createContext("/song/create", new CreateSongHandler());
        server.createContext("/comment/create", new CreateCommentHandler());
        server.createContext("/song/rate", new GradeSongHandler());
        server.createContext("/user/delete", new DeleteUserHandler());
        server.createContext("/concert", new GetConcertHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.log(Level.INFO, "Server started successfully");
    }
}
