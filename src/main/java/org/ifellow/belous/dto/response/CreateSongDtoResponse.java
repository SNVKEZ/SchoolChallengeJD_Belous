package org.ifellow.belous.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CreateSongDtoResponse {
    String success_message;
    String login;
    String song;
    String time;
}
