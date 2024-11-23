package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.SongDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.model.Song;

import javax.xml.crypto.Data;
import java.util.stream.Collectors;

public class SongDaoImpl implements SongDao {
    @Override
    public void create(SongCreateDtoRequest song, String login) {
        Database.songs.add(Song.builder()
                .id(String.valueOf(Database.songs.size()+1))
                .authors(song.getAuthors())
                .name(song.getName())
                .composers(song.getComposers())
                .duration(song.getDuration())
                .executor(song.getExecutor())
                .user(login)
                .grade(5)
                .countGrade((int) Database.songs.stream()
                        .filter(songs -> songs.getName().equals(song.getName())).count() + 1)
                .averageGrade((int) Database.songs.stream()
                        .filter(songs -> songs.getName().equals(song.getName())) // Фильтруем по имени
                        .mapToInt(Song::getGrade) // Извлекаем оценки
                        .average() // Вычисляем среднее
                        .orElse(5))
                .build());
    }

    @Override
    public String idSongByExecutorAndName(String executor, String name) {
        return String.valueOf(Database.songs.stream()
                .filter(song -> song.getExecutor().equals(executor) && song.getName().equals(name))
                .map(Song::getId).findFirst());
    }
}
