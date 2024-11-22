package org.ifellow.belous;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;
import org.ifellow.belous.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Server
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserService userService = new UserService();

    public static void main(String[] args) throws IOException {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest();
        user.setLogin("snvkez");
        user.setName("Alexei");
        user.setPassword("shcvora");
        user.setSurname("Belous");

        userService.create(user);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", new RegisterHandler());
        server.setExecutor(null); // Используется дефолтный пул потоков
        server.start();
    }

    // Обработчик для регистрации пользователя
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    System.out.println(userService.getUserByLogin("snvkez"));
                    // Десериализация JSON в DTO
                    RegisterUserDtoRequest userDto = objectMapper.readValue(exchange.getRequestBody(), RegisterUserDtoRequest.class);

                    // Регистрация через UserService
                    String result = userService.create(userDto);

                    System.out.println(userDto.getName());;
                    // Формирование JSON-ответа
                    Map<String, Object> response = new HashMap<>();
                    if (result.startsWith("ErrorExist")){
                        response.put("error", "Пользователь уже существует");
                        sendJsonResponse(exchange, response, 400);
                    } else
                    if (result.startsWith("Error")) {
                        response.put("error", result);
                        sendJsonResponse(exchange, response, 400);
                    } else {
                        response.put("message", "Пользователь создан");
                        sendJsonResponse(exchange, response, 201);
                    }
                } catch (Exception e) {
                    System.out.println(e);
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
