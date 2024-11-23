package org.ifellow.belous.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RegisterUserDtoRequest {
    String login;
    String password;
    String name;
    String surname;
}