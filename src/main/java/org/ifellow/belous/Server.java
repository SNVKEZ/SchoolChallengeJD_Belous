package org.ifellow.belous;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.exceptions.NotExistUserException;
import org.ifellow.belous.exceptions.RegisterException;
import org.ifellow.belous.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserService userService = new UserService();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/authorization", new AuthorizationHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.log(Level.INFO, "Server started");
    }

    // Обработчик для регистрации пользователя
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    // Десериализация JSON в DTO
                    RegisterUserDtoRequest userDto = objectMapper.readValue(exchange.getRequestBody(), RegisterUserDtoRequest.class);

                    Map<String, Object> response = new HashMap<>();
                    try {
                        userService.create(userDto);
                        response.put("message", "Пользователь создан");
                        sendJsonResponse(exchange, response, 201);
                    } catch (RegisterException exception) {
                        response.put("error", "Пользователь уже существует");
                        sendJsonResponse(exchange, response, 400);
                    }
                } catch (Exception e) {
                    sendErrorResponse(exchange, "Invalid request", 400);
                }
            } else {
                sendErrorResponse(exchange, "Method Not Allowed", 405);
            }
        }
    }

    // Обработчик для регистрации пользователя
    static class AuthorizationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    // Десериализация JSON в DTO
                    LoginDtoRequest userDto = objectMapper.readValue(exchange.getRequestBody(), LoginDtoRequest.class);

                    Map<String, Object> response = new HashMap<>();
                    try {
                        String ID = userService.authorization(userDto);
                        response.put("message", "Вход успешный");
                        response.put("token", ID);
                        sendJsonResponse(exchange, response, 200);
                    } catch (NotExistUserException exception) {
                        response.put("error", exception.getMessage() + " " + userDto.getLogin());
                        sendJsonResponse(exchange, response, 400);
                    }
                } catch (Exception e) {
                    sendErrorResponse(exchange, "Invalid request", 400);
                }
            } else {
                sendErrorResponse(exchange, "Method Not Allowed", 405);
            }
        }
    }

    // Метод для отправки JSON-ответа
    private static void sendJsonResponse(HttpExchange exchange, Map<String, ?> response, int statusCode) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    // Метод для отправки JSON-ошибки
    private static void sendErrorResponse(HttpExchange exchange, String errorMessage, int statusCode) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        sendJsonResponse(exchange, response, statusCode);
    }
}
