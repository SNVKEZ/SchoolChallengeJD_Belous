package org.ifellow.belous.service;

import org.ifellow.belous.daoimpl.SongDaoImpl;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.exceptions.song.NotValidSongException;
import org.ifellow.belous.model.Song;

public class SongService {
    private final SongDaoImpl songDao = new SongDaoImpl();

    public void create(SongCreateDtoRequest song, String login){
        if (song.getDuration()<=0 || song.getDuration()>3600){
            throw new NotValidSongException("Неправильная валидация поля ","duration");
        }else {
            songDao.create(song, login);
        }
    }

    public String getIdByExecutorAndName(String executor, String name){
        return songDao.idSongByExecutorAndName(executor,name);
    }
}
