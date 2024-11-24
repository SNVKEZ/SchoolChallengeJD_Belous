package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.dto.response.RegisterUserDtoResponse;
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
                RegisterUserDtoRequest userDto = objectMapper.readValue(exchange.getRequestBody(), RegisterUserDtoRequest.class);
                RegisterUserDtoResponse userDtoResponse = new RegisterUserDtoResponse();
                Map<String, Object> response = new HashMap<>();
                try {
                    isValid(userDto);
                    userService.create(userDto);
                    userDtoResponse.setLogin(userDto.getLogin());
                    userDtoResponse.setSuccess_message("Пользователь создан");
                    userDtoResponse.setTime(timeNow);
                    String jsonResponse = objectMapper.writeValueAsString(userDtoResponse);
                    sendJsonResponse(exchange, jsonResponse, 201);
                } catch (RegisterException exception) {
                    ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                    ERROR_DTO_RESPONSE.setTime(timeNow);
                    String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                    sendJsonResponse(exchange, jsonResponse, 409);
                } catch (NotValidDataException exception){
                    ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                    ERROR_DTO_RESPONSE.setTime(timeNow);
                    String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                    sendJsonResponse(exchange, jsonResponse, 400);
                }
            } catch (Exception e) {
                ERROR_DTO_RESPONSE.setError_message("Invalid request");
                ERROR_DTO_RESPONSE.setTime(timeNow);
                String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                sendJsonResponse(exchange, jsonResponse, 400);
            }
        } else {
            ERROR_DTO_RESPONSE.setError_message("Method Not Allowed");
            ERROR_DTO_RESPONSE.setTime(timeNow);
            String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
            sendJsonResponse(exchange, jsonResponse, 405);
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

