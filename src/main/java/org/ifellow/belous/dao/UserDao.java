package org.ifellow.belous.dao;

import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;

public interface UserDao {
    void create(RegisterUserDtoRequest user);
    boolean checkExistUser(String login);
    boolean authorization(LoginDtoRequest user, String login);
    String getLoginByToken(String token);
    void logOutUser(String token);
    boolean checkActiveSession(String login);
    boolean checkActiveSessionByToken(String token);

    void recordSession(String login, String ID);

    void deleteUser(String login);
}
