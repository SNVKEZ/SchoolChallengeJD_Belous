package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.exceptions.user.AutorizeYetException;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationHandler extends MainHandler {
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
                } catch (AutorizeYetException e){
                    response.put("error", e.getMessage());
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
