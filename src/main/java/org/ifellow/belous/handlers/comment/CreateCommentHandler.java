package org.ifellow.belous.handlers.comment;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.exceptions.song.NotExistSongByNameAndExecutorException;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateCommentHandler extends MainHandler {
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
                    CommentCreateDtoRequest comment = objectMapper.readValue(exchange.getRequestBody(), CommentCreateDtoRequest.class);

                    try {
                        isValid(comment);
                        commentService.create(comment, userService.getLoginByToken(authHeader),
                                songService.getIdByExecutorAndName(comment.getExecutor(), comment.getName()));
                        response.put("message", "Комментарий успешно создан");
                        sendJsonResponse(exchange, response, 200);
                    } catch (NotExistSongByNameAndExecutorException | NotExistUserException exception){
                        response.put("error", exception.getMessage());
                        sendJsonResponse(exchange, response, 404);
                    }
                    catch (NotValidSongException exception){
                        response.put("error", exception.getMessage());
                        sendJsonResponse(exchange, response, 403);
                    } catch (NotValidDataException e){
                        response.put("error", e.getMessage());
                        sendJsonResponse(exchange, response, 400);
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
    private void isValid(CommentCreateDtoRequest comment){
        if(!(comment != null
                && comment.getCommentaries()!=null && !comment.getCommentaries().isEmpty()
                && comment.getName()!=null && !comment.getName().isEmpty()
                && comment.getExecutor()!=null && !comment.getExecutor().isEmpty())){
            throw new NotValidDataException("Неправильная валидация запроса");
        }
    }
}
