package org.ifellow.belous.service;

import org.ifellow.belous.daoimpl.UserDaoImpl;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.exceptions.AutorizeYetException;
import org.ifellow.belous.exceptions.NotExistTokenSession;
import org.ifellow.belous.exceptions.NotExistUserException;
import org.ifellow.belous.exceptions.RegisterException;

import java.util.UUID;

public class UserService {

    private final UserDaoImpl userDao = new UserDaoImpl();

    public void create(RegisterUserDtoRequest newUser) {
        if (!userDao.checkExistUser(newUser.getLogin())) {
            new UserDaoImpl().create(newUser);
        } else {
            throw new RegisterException("Пользователь с таким именем существует");
        }
    }

    public String authorization(LoginDtoRequest loginUser) {
        String ID = "";
        if (!userDao.checkExistUser(loginUser.getLogin())) {
            throw new NotExistUserException("Пользователь не найден");
        }
        if (!userDao.authorization(loginUser, loginUser.getLogin())) {
            throw new NotExistUserException("Не верный пароль");
        }
        if (userDao.checkActiveSession(loginUser.getLogin())) {
            throw new AutorizeYetException("Пользователь уже авторизован");
        } else {
            ID = UUID.randomUUID().toString();
            userDao.recordSession(loginUser.getLogin(), ID);
        }
        return ID;
    }

    public void logOut(String token) {
        if (userDao.checkActiveSessionByToken(token)) {
            userDao.logOutUser(token);
        } else {
            throw new NotExistTokenSession("Несуществующая сессия");
        }

    }
}
