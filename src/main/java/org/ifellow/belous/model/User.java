package org.ifellow.belous.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Setter
@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String id;
    String login;
    String password;
    String name;
    String surname;
}
