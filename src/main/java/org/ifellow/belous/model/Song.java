package org.ifellow.belous.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
@Value
@Builder
public class Song {
    String name;
    List<String> composers;
    List<String> authors;
    String executor;
    int duration;
}
