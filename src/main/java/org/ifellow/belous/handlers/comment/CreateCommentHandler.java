package org.ifellow.belous.handlers.comment;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.dto.response.CreateCommentDtoResponse;
import org.ifellow.belous.exceptions.song.NotExistSongByNameAndExecutorException;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.NotValidDataException;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateCommentHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                // Получение заголовков
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                CreateCommentDtoResponse response = new CreateCommentDtoResponse();
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
                        String id = commentService.create(comment, userService.getLoginByToken(authHeader),
                                songService.getIdByExecutorAndName(comment.getExecutor(), comment.getName()));
                        songService.addCommentToSong(comment.getExecutor(), comment.getName(), id);
                        response.setSong(comment.getExecutor() + " - " + comment.getName());
                        response.setLogin(userService.getLoginByToken(authHeader));
                        response.setTime(timeNow);
                        response.setSuccess_message("Комментарий успешно создан");
                        String jsonResponse = objectMapper.writeValueAsString(response);
                        sendJsonResponse(exchange, jsonResponse, 200);
                    } catch (NotExistSongByNameAndExecutorException | NotExistUserException exception){
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 404);
                    }
                    catch (NotValidSongException exception){
                        ERROR_DTO_RESPONSE.setError_message(exception.getMessage());
                        ERROR_DTO_RESPONSE.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")));
                        String jsonResponse = objectMapper.writeValueAsString(ERROR_DTO_RESPONSE);
                        sendJsonResponse(exchange, jsonResponse, 403);
                    } catch (NotValidDataException e){
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
                ERROR_DTO_RESPONSE.setError_message(e.getMessage());
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
    private void isValid(CommentCreateDtoRequest comment){
        if(!(comment != null
                && comment.getCommentaries()!=null && !comment.getCommentaries().isEmpty()
                && comment.getName()!=null && !comment.getName().isEmpty()
                && comment.getExecutor()!=null && !comment.getExecutor().isEmpty())){
            throw new NotValidDataException("Неправильная валидация запроса");
        }
    }
}
