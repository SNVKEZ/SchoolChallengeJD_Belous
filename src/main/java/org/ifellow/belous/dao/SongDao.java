package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.RateSongDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.model.Song;

import java.util.List;

public interface SongDao {
    void create(SongCreateDtoRequest song, String login);
    String idSongByExecutorAndName(String executor, String name);
    void addCommentToSong(String executor, String name, String idComment);
    void rateSong(String login, RateSongDtoRequest grade);
    boolean checkSongThatUserNotOwner(String login, String idSong);
    void deleteSongByLogin(String login);
    void deleteRateByLogin(String login);

    List<Song> getConcert();
}
