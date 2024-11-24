package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.response.LoginDtoResponse;
import org.ifellow.belous.exceptions.user.AutorizeYetException;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;

public class AuthorizationHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                // Десериализация JSON в DTO
                LoginDtoRequest userDto = objectMapper.readValue(exchange.getRequestBody(), LoginDtoRequest.class);
                LoginDtoResponse loginDtoResponse = new LoginDtoResponse();
                try {
                    String ID = userService.authorization(userDto);
                    loginDtoResponse.setSuccess_message("Вход успешный");
                    loginDtoResponse.setLogin(userService.getLoginByToken(ID));
                    loginDtoResponse.setToken(ID);
                    loginDtoResponse.setTime(timeNow);
                    // Преобразуем объект в JSON
                    String jsonResponse = objectMapper.writeValueAsString(loginDtoResponse);
                    sendJsonResponse(exchange, jsonResponse, 200);
                } catch (NotExistUserException exception) {
                    ERROR_DTO_RESPONSE.setError_message(exception.getMessage() + " " + userDto.getLogin());
                    ERROR_DTO_RESPONSE.setTime(timeNow);
                    String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                    sendJsonResponse(exchange, jsonResponse, 404);
                } catch (AutorizeYetException e){
                    ERROR_DTO_RESPONSE.setError_message(e.getMessage() + " " + userDto.getLogin());
                    ERROR_DTO_RESPONSE.setTime(timeNow);
                    String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                    sendJsonResponse(exchange, jsonResponse, 400);
                }
            } catch (Exception e) {
                ERROR_DTO_RESPONSE.setError_message("Invalid Request");
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
}
