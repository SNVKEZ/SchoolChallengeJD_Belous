package org.ifellow.belous.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
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