package org.ifellow.belous.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginDtoResponse {
    String success_message;
    String time;
    String login;
    String token;
}