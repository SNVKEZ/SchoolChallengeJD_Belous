package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.exceptions.user.RegisterException;
import org.ifellow.belous.handlers.MainHandler;

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
                    isValid(userDto);
                    userService.create(userDto);
                    response.put("message", "Пользователь создан");
                    sendJsonResponse(exchange, response, 201);
                } catch (RegisterException exception) {
                    response.put("error", "Пользователь уже существует");
                    sendJsonResponse(exchange, response, 400);
                } catch (NotValidDataException exception){
                    response.put("error", "Неправильная валидация отправленного json-а");
                    sendJsonResponse(exchange, response, 400);
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, "Invalid request", 400);
            }
        } else {
            sendErrorResponse(exchange, "Method Not Allowed", 405);
        }
    }

    private void isValid(RegisterUserDtoRequest user){
        if(!( user != null
                && user.getLogin()!=null && !user.getLogin().isEmpty()
                && user.getName()!=null && !user.getName().isEmpty()
                && user.getPassword()!=null && !user.getPassword().isEmpty()
                && user.getSurname()!=null && !user.getSurname().isEmpty())){
            throw new NotValidDataException("Неправильная валидация запроса");
        }
    }
}

