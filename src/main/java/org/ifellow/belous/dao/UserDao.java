package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

public interface UserDao {

    void create(RegisterUserDtoRequest user);

    boolean checkExistUser(String login);

    boolean authorization(LoginDtoRequest user, String login);

    User getUserByLogin(String login);

    public void recordSession(String ID);
}
