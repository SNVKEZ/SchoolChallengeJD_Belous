package org.ifellow.belous.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.ifellow.belous.model.Song;

import java.util.List;

@Data
@Setter
@Getter
public class GetConcertDtoResponse {
    List<Song> songs;
    String time;
}
