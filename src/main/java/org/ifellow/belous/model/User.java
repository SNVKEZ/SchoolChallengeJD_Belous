package org.ifellow.belous.model;

import lombok.*;

@Data
@Value
@Builder
@Setter
@Getter
public class User {
    String id;
    String login;
    String password;
    String name;
    String surname;
}
