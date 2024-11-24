package org.ifellow.belous.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ErrorDtoResponse {
    String error_message;
    String time;
}
