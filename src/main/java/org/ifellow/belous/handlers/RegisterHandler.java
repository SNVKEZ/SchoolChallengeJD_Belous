package org.ifellow.belous.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.exceptions.RegisterException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterHandler extends MainHandler {

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

