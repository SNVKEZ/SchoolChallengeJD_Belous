package org.ifellow.belous.service;

import org.ifellow.belous.daoimpl.CommentDaoImpl;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.dto.request.SongCreateDtoRequest;
import org.ifellow.belous.exceptions.comment.NotValidCommentsExceptions;
import org.ifellow.belous.exceptions.song.NotValidSongException;

public class CommentService {
    private final CommentDaoImpl commentDao = new CommentDaoImpl();

    public void create(CommentCreateDtoRequest comment, String login, String idSong){
        if (!comment.getCommentaries().stream()
                .allMatch(s -> s.length() >= 5 && s.length() <= 200)){
            throw new NotValidCommentsExceptions("Неправильная валидация комментариев");
        }else {
            commentDao.create(comment, login, idSong);
        }
    }
}
