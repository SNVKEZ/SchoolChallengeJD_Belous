package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.CommentCreateDtoRequest;

public interface CommentDao {
    String create(CommentCreateDtoRequest commentCreateDtoRequest, String login, String idSong);

    void deleteCommentsByLogin(String login);
}
