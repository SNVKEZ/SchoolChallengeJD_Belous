package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

public interface UserDao {

    String create(RegisterUserDtoRequest user);

    User getUserByLogin(String login);
}
