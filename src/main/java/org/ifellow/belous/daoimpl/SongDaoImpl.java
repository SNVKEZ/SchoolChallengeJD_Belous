package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.SongDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.model.Song;
import org.ifellow.belous.model.User;

public class SongDaoImpl implements SongDao {
    @Override
    public void create(SongCreateDtoRequest song) {
        Database.songs.add(Song.builder()
                        .authors(song.getAuthors())
                        .name(song.getName())
                        .composers(song.getComposers())
                        .duration(song.getDuration())
                        .executor(song.getExecutor())
                .build());
    }
}
