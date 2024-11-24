package org.ifellow.belous.handlers.song;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.RateSongDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.dto.response.GradeSongDtoResponse;
import org.ifellow.belous.exceptions.song.NotExistSongByNameAndExecutorException;
import org.ifellow.belous.exceptions.song.NotModifyGradeSongByOwnerException;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GradeSongHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                GradeSongDtoResponse response = new GradeSongDtoResponse();
                // Получение заголовков
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                if (authHeader == null || authHeader.isEmpty()) {
                    // Если заголовок отсутствует
                    throw new NotExistTokenSession("Неавтаризованный пользователь");
                }
                try {
                    userService.checkAuthorization(authHeader);
                    // Десериализация JSON в DTO
                    RateSongDtoRequest song = objectMapper.readValue(exchange.getRequestBody(), RateSongDtoRequest.class);

                    try {
                        songService.rateSong(userService.getLoginByToken(authHeader), song);
                        response.setSuccess_message("Оценка песни успешно выставлена");
                        response.setSong(song.getExecutor() + " - " + song.getName());
                        response.setLogin(userService.getLoginByToken(authHeader));
                        response.setTime(timeNow);
                        response.setGrade(song.getGrade());
                        String jsonResponse = objectMapper.writeValueAsString(response);
                        sendJsonResponse(exchange, jsonResponse, 200);
                    } catch (NotModifyGradeSongByOwnerException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 422);
                    } catch (NotExistUserException | NotExistSongByNameAndExecutorException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 404);
                    } catch (NotValidSongException exception) {
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 403);
                    } catch (NotValidDataException e) {
                        ERROR_DTO_RESPONSE.setError_message(e.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 400);
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
