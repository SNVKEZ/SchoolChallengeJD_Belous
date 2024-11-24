package org.ifellow.belous.handlers.song;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.dto.response.CreateSongDtoResponse;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.exceptions.song.SongYetExistException;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateSongHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                CreateSongDtoResponse response = new CreateSongDtoResponse();

                if (authHeader == null || authHeader.isEmpty()) {
                    throw new NotExistTokenSession("Неавтаризованный пользователь");
                }
                try {
                    userService.checkAuthorization(authHeader);
                    SongCreateDtoRequest song = objectMapper.readValue(exchange.getRequestBody(), SongCreateDtoRequest.class);

                    try {
                        isValid(song);
                        songService.create(song, userService.getLoginByToken(authHeader));
                        response.setSong(song.getExecutor() + " - " + song.getName());
                        response.setLogin(userService.getLoginByToken(authHeader));
                        response.setSuccess_message("Песня успешно создана");
                        response.setTime(timeNow);
                        String jsonResponse = objectMapper.writeValueAsString(response);
                        sendJsonResponse(exchange, jsonResponse, 200);
                    } catch (SongYetExistException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 409);
                    } catch (NotExistUserException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 400);
                    } catch (NotValidSongException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 403);
                    } catch (NotValidDataException e) {
                        ERROR_DTO_RESPONSE.setError_message(e.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 404);
                    }
                } catch (NotExistTokenSession existTokenSession) {
                    ERROR_DTO_RESPONSE.setError_message(existTokenSession.getMessage());
                    ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                    String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                    sendJsonResponse(exchange, jsonResponse, 400);
                }
            } catch (NotExistTokenSession tokenSession) {
                ERROR_DTO_RESPONSE.setError_message(tokenSession.getMessage());
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

    private void isValid(SongCreateDtoRequest song) {
        if (!(song != null
                && song.getComposers() != null && !song.getComposers().isEmpty()
                && song.getName() != null && !song.getName().isEmpty()
                && song.getAuthors() != null && !song.getAuthors().isEmpty()
                && song.getExecutor() != null && !song.getExecutor().isEmpty())) {
            throw new NotValidDataException("Неправильная валидация запроса");
        }
    }
}
