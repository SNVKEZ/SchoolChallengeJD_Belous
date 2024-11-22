package org.ifellow.belous.dto.request;

import lombok.*;

@Data
//@Value
//@Builder
@Setter
@Getter
public class RegisterUserDtoRequest {
    String login;
    String password;
    String name;
    String surname;
}