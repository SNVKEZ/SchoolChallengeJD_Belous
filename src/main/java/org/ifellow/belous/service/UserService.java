package org.ifellow.belous.service;

import org.ifellow.belous.daoimpl.UserDaoImpl;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

public class UserService {
    public String create(RegisterUserDtoRequest newUser){
        return new UserDaoImpl().create(newUser);
    }

    public User getUserByLogin(String login){
        return new UserDaoImpl().getUserByLogin(login);
    }
}
