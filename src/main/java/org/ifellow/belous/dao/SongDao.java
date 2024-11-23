package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.model.Song;

public interface SongDao {
    void create(SongCreateDtoRequest song, String login);
    String idSongByExecutorAndName(String executor, String name);
}
