package org.ifellow.belous.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class GradeSongDtoResponse {
    String success_message;
    String login;
    String song;
    int grade;
    String time;
}
