package org.ifellow.belous.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ifellow.belous.dto.response.ErrorDtoResponse;
import org.ifellow.belous.service.CommentService;
import org.ifellow.belous.service.SongService;
import org.ifellow.belous.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainHandler implements HttpHandler {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static final UserService userService = new UserService();
    protected static final SongService songService = new SongService();
    protected static final CommentService commentService = new CommentService();
    protected static final ErrorDtoResponse ERROR_DTO_RESPONSE = new ErrorDtoResponse();
    protected static final String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss"));


    // Метод для отправки JSON-ответа
    protected static void sendJsonResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Метод для извлечения параметров запроса в виде Map
    protected Map<String, String> getQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
