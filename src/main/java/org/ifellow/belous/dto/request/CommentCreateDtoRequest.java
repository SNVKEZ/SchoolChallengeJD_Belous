package org.ifellow.belous.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class CommentCreateDtoRequest {
    String name;
    String executor;
    List<String> commentaries;
}
