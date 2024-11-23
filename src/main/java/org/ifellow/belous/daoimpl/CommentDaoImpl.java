package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.CommentDao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.model.Comment;
import org.ifellow.belous.model.Song;

public class CommentDaoImpl implements CommentDao {
    @Override
    public void create(CommentCreateDtoRequest comment, String login, String idSong) {
        Database.comments.add(Comment.builder()
                .id(String.valueOf(Database.songs.size()+1))
                .user(login)
                        .name(comment.getName())
                        .executor(comment.getExecutor())
                        .commentaries(comment.getCommentaries())
                        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")))
                        .idSong(idSong)
                .build());
    }
}
