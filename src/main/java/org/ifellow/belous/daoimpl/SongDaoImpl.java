package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.SongDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.RateSongDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.model.Song;

import java.util.*;

public class SongDaoImpl implements SongDao {

    private void sort() {
        Database.songs.sort(Comparator.comparingDouble(Song::getAverageGrade).reversed() // По averageGrade по убыванию
                .thenComparingInt(Song::getDuration));
    }

    @Override
    public void create(SongCreateDtoRequest song, String login) {
        Map<String, Integer> rate = new HashMap<>();
        rate.put(login, 5);
        Database.songs.add(Song.builder()
                .id(String.valueOf(Database.songs.size() + 1))
                .authors(song.getAuthors())
                .name(song.getName())
                .composers(song.getComposers())
                .duration(song.getDuration())
                .executor(song.getExecutor())
                .user(login)
                .grade(5)
                .rating(rate)
                .countGrade(rate.size())
                .averageGrade(5)
                .idComment(new ArrayList<>())
                .build());
        sort();
    }

    @Override
    public String idSongByExecutorAndName(String executor, String name) {
        return Database.songs.stream()
                .filter(song -> song.getExecutor().equals(executor) && song.getName().equals(name))
                .map(Song::getId)  // Извлекаем id
                .findFirst().orElse(null);
    }

    @Override
    public void addCommentToSong(String executor, String name, String idComment) {
        Database.songs.stream()
                .filter(song -> song.getExecutor().equals(executor) && song.getName().equals(name))
                .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).getIdComment().add(idComment);
        sort();
    }

    @Override
    public void rateSong(String login, RateSongDtoRequest grade) {
        Database.songs.stream()
                .filter(song -> song.getExecutor().equals(grade.getExecutor()) && song.getName().equals(grade.getName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).getRating().put(login, grade.getGrade());
        Database.songs.stream()
                .filter(song -> song.getExecutor().equals(grade.getExecutor()) && song.getName().equals(grade.getName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).setCountGrade(Database.songs.stream()
                        .filter(song -> song.getExecutor().equals(grade.getExecutor()) && song.getName().equals(grade.getName()))
                        .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).getRating().size());
        Database.songs.stream()
                .filter(song -> song.getExecutor().equals(grade.getExecutor()) && song.getName().equals(grade.getName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).setAverageGrade(
                        Math.round(Database.songs.stream()
                                .filter(song -> song.getExecutor().equals(grade.getExecutor()) && song.getName().equals(grade.getName()))
                                .findFirst().orElseThrow(() -> new RuntimeException("Песня не найдена")).getRating()
                                .values().stream()
                                .mapToInt(Integer::intValue) // Преобразуем значения в IntStream
                                .average()                  // Вычисляем среднее
                                .orElse(0.0) * 100.0) / 100.0);
        sort();
    }

    @Override
    public boolean checkSongThatUserNotOwner(String login, String idSong) {
        return Database.songs.stream()
                .filter(song -> song.getId().equals(idSong) && song.getUser().equals(login))
                .findFirst().isEmpty();
    }

    @Override
    public void deleteSongByLogin(String login) {
        Database.songs.removeIf(song -> song.getUser().equals(login) && song.getRating().size() == 1);
        sort();
    }

    @Override
    public void deleteRateByLogin(String login) {
        Database.songs.stream()
                .filter(song -> song.getUser().equals(login) && song.getRating().size() > 1)
                .forEach(song -> song.getRating().remove(login));
        sort();
    }

    @Override
    public List<Song> getConcert() {
        List<Song> result = new ArrayList<>();
        int[] currentSum = {0};
        Database.songs.stream()
                .takeWhile(song -> {
                    int nextDuration = song.getDuration();
                    if (!result.isEmpty()) {
                        nextDuration += 10; // Добавляем 10 секунд, если это не первый элемент
                    }
                    if (currentSum[0] + nextDuration <= 3600) {
                        currentSum[0] += nextDuration;
                        return true;
                    }
                    return false;
                })
                .forEach(result::add);
        return result;
    }
}
