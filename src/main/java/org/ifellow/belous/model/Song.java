package org.ifellow.belous.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Value
@Builder
@Getter
@Setter
public class Song {
    String id;
    String name;
    List<String> composers;
    List<String> authors;
    String executor;
    int duration;
    String user;
    int grade;
    int averageGrade;
    int countGrade;
    Map<String, String> comment;
}
