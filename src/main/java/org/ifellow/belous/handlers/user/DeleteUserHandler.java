package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.response.DeleteUserDtoResponse;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DeleteUserHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            URI requestUri = exchange.getRequestURI();
            Map<String, String> queryParams = getQueryParams(requestUri.getQuery());
            String login = queryParams.get("login");
            // Получение заголовков
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                // Если заголовок отсутствует
                sendErrorResponse(exchange, "Missing Authorization header", 401);
                return;
            }

            DeleteUserDtoResponse response = new DeleteUserDtoResponse();
            try {
                userService.deleteUser(login, userService.getLoginByToken(authHeader));
                userService.logOut(authHeader);
                songService.deleteSongAndRateDeletedUser(login);
                commentService.deleteCommentsByLogin(login);
                response.setLogin(login);
                response.setSuccess_message("Пользователь успешно удален");
                response.setTime(timeNow);
                String jsonResponse = objectMapper.writeValueAsString(response);
                sendJsonResponse(exchange, jsonResponse, 200);
            } catch (NotExistTokenSession existTokenSession) {
                ERROR_DTO_RESPONSE.setError_message(existTokenSession.getMessage());
                ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                sendJsonResponse(exchange, jsonResponse, 400);
            } catch (Exception e) {
                ERROR_DTO_RESPONSE.setError_message("Invalid request");
                ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                sendJsonResponse(exchange, jsonResponse, 400);
            }
        } else {
            ERROR_DTO_RESPONSE.setError_message("Method Not Allowed");
            ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
            String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
            sendJsonResponse(exchange, jsonResponse, 405);
        }
    }
}
