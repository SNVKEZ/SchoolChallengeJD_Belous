package org.ifellow.belous.handlers.song;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateSongHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, Object> response = new HashMap<>();
            try {
                // Получение заголовков
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                if (authHeader == null || authHeader.isEmpty()) {
                    // Если заголовок отсутствует
                    throw new NotExistTokenSession("Неавтаризованный пользователь");
                }
                try {
                    userService.checkAuthorization(authHeader);

                // Десериализация JSON в DTO
                SongCreateDtoRequest song = objectMapper.readValue(exchange.getRequestBody(), SongCreateDtoRequest.class);

                try {
                    isValid(song);
                    songService.create(song, userService.getLoginByToken(authHeader));
                    response.put("message", "Песня успешно создана");
                    sendJsonResponse(exchange, response, 200);
                } catch (NotExistUserException exception) {
                    response.put("error", exception.getMessage());
                    sendJsonResponse(exchange, response, 400);
                } catch (NotValidSongException exception){
                    response.put("error", exception.getMessage());
                    sendJsonResponse(exchange, response, 403);
                } catch (NotValidDataException e){
                    response.put("error", e.getMessage());
                    sendJsonResponse(exchange, response, 404);
                }
                } catch (NotExistTokenSession existTokenSession) {
                    response.put("error", existTokenSession.getMessage());
                    sendJsonResponse(exchange, response, 400);
                }
            } catch (NotExistTokenSession tokenSession) {
                response.put("error", tokenSession.getMessage());
                sendJsonResponse(exchange, response, 400);
            } catch (Exception e) {
                sendErrorResponse(exchange, "Invalid request", 400);
            }
        } else {
            sendErrorResponse(exchange, "Method Not Allowed", 405);
        }
    }

    private void isValid(SongCreateDtoRequest song){
        if(!(song != null
                && song.getComposers()!=null && !song.getComposers().isEmpty()
                && song.getName()!=null && !song.getName().isEmpty()
                && song.getAuthors()!=null && !song.getAuthors().isEmpty()
                && song.getExecutor()!=null && !song.getExecutor().isEmpty())){
            throw new NotValidDataException("Неправильная валидация запроса");
        }
    }
}
