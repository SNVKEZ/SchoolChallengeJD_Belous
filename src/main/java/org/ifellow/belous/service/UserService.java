package org.ifellow.belous.service;

import org.ifellow.belous.daoimpl.UserDaoImpl;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
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
        } else {
            ID = UUID.randomUUID().toString();
            userDao.recordSession(ID);
        }
        return ID;
    }
}
