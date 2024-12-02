package org.ifellow.belous.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Getter
@Setter
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String id;
    String user;
    String name;
    String executor;
    List<String> commentaries;
    String time;
    String idSong;
}