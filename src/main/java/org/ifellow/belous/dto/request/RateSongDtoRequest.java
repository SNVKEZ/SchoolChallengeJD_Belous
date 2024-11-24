package org.ifellow.belous.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RateSongDtoRequest {
    String executor;
    String name;
    int grade;
}
