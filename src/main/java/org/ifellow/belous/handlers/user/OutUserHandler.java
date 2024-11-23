package org.ifellow.belous.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.handlers.MainHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutUserHandler extends MainHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Получение заголовков
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            if (authHeader == null || authHeader.isEmpty()) {
                // Если заголовок отсутствует
                sendErrorResponse(exchange, "Missing Authorization header", 401);
                return;
            }

            Map<String, Object> response = new HashMap<>();
            try {
                userService.logOut(authHeader);
                response.put("message", "Access granted");
                sendJsonResponse(exchange, response, 200);
            } catch (NotExistTokenSession existTokenSession) {
                response.put("error", existTokenSession.getMessage());
                sendJsonResponse(exchange, response, 400);
            }
        } else {
            // Неподдерживаемый метод
            sendErrorResponse(exchange, "Method Not Allowed", 405);

        }
    }
}
