package org.ifellow.belous.handlers.song;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.response.GetConcertDtoResponse;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.handlers.MainHandler;
import org.ifellow.belous.model.Song;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetConcertHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Получение заголовков
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                ERROR_DTO_RESPONSE.setError_message("Missing Authorization header");
                ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                sendJsonResponse(exchange, jsonResponse, 401);
                return;
            }

            GetConcertDtoResponse response = new GetConcertDtoResponse();
            try {
                List<Song> songs = songService.getConcert();
                response.setSongs(songs);
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
