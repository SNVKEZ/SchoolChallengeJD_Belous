package org.ifellow.belous.service;

import org.ifellow.belous.dao.UserDao;
import org.ifellow.belous.daoimpl.UserDaoImpl;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.exceptions.user.AutorizeYetException;
import org.ifellow.belous.exceptions.user.NotExistTokenSession;
import org.ifellow.belous.exceptions.user.NotExistUserException;
import org.ifellow.belous.exceptions.user.RegisterException;

import java.util.UUID;

public class UserService {

    private final UserDao userDao = new UserDaoImpl();

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

    public String getLoginByToken(String token){
       return userDao.getLoginByToken(token);
    }
    public void checkAuthorization(String token){
        if(!userDao.checkActiveSessionByToken(token)){
            throw new NotExistTokenSession("Несуществующая сессия");
        }
    }
    public void logOut(String token) {
        if (userDao.checkActiveSessionByToken(token)) {
            userDao.logOutUser(token);
        } else {
            throw new NotExistTokenSession("Несуществующая сессия");
        }

    }

    public void deleteUser(String login, String token) {
        if (login.equals(token)) {
            if (!userDao.checkExistUser(login)) {
                throw new NotExistUserException("Пользователь не найден");
            } else userDao.deleteUser(login);
        }
    }
}
