package org.ifellow.belous.service;

import org.ifellow.belous.dao.CommentDao;
import org.ifellow.belous.daoimpl.CommentDaoImpl;
import org.ifellow.belous.dto.request.CommentCreateDtoRequest;
import org.ifellow.belous.exceptions.comment.NotValidCommentsExceptions;

public class CommentService {
    private final CommentDao commentDao = new CommentDaoImpl();

    public String create(CommentCreateDtoRequest comment, String login, String idSong) {
        String idComment = null;
        if (!comment.getCommentaries().stream()
                .allMatch(s -> s.length() >= 5 && s.length() <= 200)){
            throw new NotValidCommentsExceptions("Неправильная валидация комментариев");
        }else {
            idComment = commentDao.create(comment, login, idSong);
        }
        return idComment;
    }

    public void deleteCommentsByLogin(String login) {
        commentDao.deleteCommentsByLogin(login);
    }
}
