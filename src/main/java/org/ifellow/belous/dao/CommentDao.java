package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.CommentCreateDtoRequest;

public interface CommentDao {
    public void create(CommentCreateDtoRequest commentCreateDtoRequest, String login, String idSong);
}
