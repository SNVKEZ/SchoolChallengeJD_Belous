package org.ifellow.belous.service;

import org.ifellow.belous.dao.SongDao;
import org.ifellow.belous.daoimpl.SongDaoImpl;
import org.ifellow.belous.dto.request.RateSongDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.exceptions.song.NotExistSongByNameAndExecutorException;
import org.ifellow.belous.exceptions.song.NotModifyGradeSongByOwnerException;
import org.ifellow.belous.exceptions.song.NotValidSongException;

public class SongService {
    private final SongDao songDao = new SongDaoImpl();

    public void create(SongCreateDtoRequest song, String login){
        if (song.getDuration()<=0 || song.getDuration()>3600){
            throw new NotValidSongException("Неправильная валидация поля ","duration");
        }else {
            songDao.create(song, login);
        }
    }

    public String getIdByExecutorAndName(String executor, String name){
        String idSong;
        if(songDao.idSongByExecutorAndName(executor,name)!=null)
        {
            idSong = songDao.idSongByExecutorAndName(executor,name);
        } else {
            throw new NotExistSongByNameAndExecutorException("Песни " + executor + " - " + name + " не существует");
        }
        return idSong;
    }

    public void addCommentToSong(String executor, String name, String idComment) {
        if (songDao.idSongByExecutorAndName(executor, name) != null) {
            songDao.addCommentToSong(executor, name, idComment);
        } else {
            throw new NotExistSongByNameAndExecutorException("Песни " + executor + " - " + name + " не существует");
        }
    }

    public void rateSong(String login, RateSongDtoRequest grade) {
        if (songDao.idSongByExecutorAndName(grade.getExecutor(), grade.getName()) != null) {
            if (songDao.checkSongThatUserNotOwner(login, songDao.idSongByExecutorAndName(grade.getExecutor(), grade.getName()))) {
                songDao.rateSong(login, grade);
            } else {
                throw new NotModifyGradeSongByOwnerException("Владелец не может изменить оценку");
            }
        } else {
            throw new NotExistSongByNameAndExecutorException("Песни " + grade.getExecutor() + " - " + grade.getName() + " не существует");
        }
    }

    public void deleteSongAndRateDeletedUser(String login) {
        songDao.deleteSongByLogin(login);
        songDao.deleteRateByLogin(login);
    }
}
