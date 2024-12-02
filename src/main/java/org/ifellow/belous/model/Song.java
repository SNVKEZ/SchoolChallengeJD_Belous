package org.ifellow.belous.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Getter
@Setter
public class Song implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String id;
    String name;
    List<String> composers;
    List<String> authors;
    String executor;
    int duration;
    String user;
    int grade;
    double averageGrade;
    int countGrade;
    List<String> idComment;
    Map<String, Integer> rating;
}
