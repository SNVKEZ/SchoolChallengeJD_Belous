package org.ifellow.belous.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Value
@Builder
@Getter
@Setter
public class Comment {
    String id;
    String user;
    String name;
    String executor;
    List<String> commentaries;
    String time;
    String idSong;
}