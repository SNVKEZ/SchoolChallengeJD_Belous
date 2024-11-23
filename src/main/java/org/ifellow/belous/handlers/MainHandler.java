package org.ifellow.belous.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ifellow.belous.service.CommentService;
import org.ifellow.belous.service.SongService;
import org.ifellow.belous.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainHandler implements HttpHandler {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static final UserService userService = new UserService();
    protected static final SongService songService = new SongService();
    protected static final CommentService commentService = new CommentService();

//    protected static void checkValidRequest(Object object){
//
//    }

    // Метод для отправки JSON-ответа
    protected static void sendJsonResponse(HttpExchange exchange, Map<String, ?> response, int statusCode) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    // Метод для отправки JSON-ошибки
    protected static void sendErrorResponse(HttpExchange exchange, String errorMessage, int statusCode) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        sendJsonResponse(exchange, response, statusCode);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
