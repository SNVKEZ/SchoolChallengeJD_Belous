package org.ifellow.belous.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class SongCreateDtoRequest {
    String name;
    List<String> composers;
    List<String> authors;
    String executor;
    int duration;
}
