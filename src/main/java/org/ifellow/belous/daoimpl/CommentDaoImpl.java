package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.CommentDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.model.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDaoImpl implements CommentDao {
    @Override
    public String create(CommentCreateDtoRequest comment, String login, String idSong) {
        Database.comments.add(Comment.builder()
                .id(String.valueOf(Database.comments.size() + 1))
                .user(login)
                        .name(comment.getName())
                        .executor(comment.getExecutor())
                        .commentaries(comment.getCommentaries())
                        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-ss")))
                        .idSong(idSong)
                .build());
        return String.valueOf(Database.comments.size());
    }

    @Override
    public void deleteCommentsByLogin(String login) {
        Database.comments.removeIf(comment -> comment.getUser().equals(login));
    }
}
