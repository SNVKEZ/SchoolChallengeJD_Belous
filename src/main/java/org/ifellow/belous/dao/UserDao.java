package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

public interface UserDao {

    void create(RegisterUserDtoRequest user);

    boolean checkExistUser(String login);

    boolean authorization(LoginDtoRequest user, String login);

    User getUserByLogin(String login);

    void logOutUser(String token);

    boolean checkActiveSession(String login);
    boolean checkActiveSessionByToken(String token);

    public void recordSession(String login, String ID);
}
