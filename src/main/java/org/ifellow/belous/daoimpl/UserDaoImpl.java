package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.UserDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

import java.util.UUID;


public class UserDaoImpl implements UserDao {

    @Override
    public String create(RegisterUserDtoRequest newUser) {
        String id = String.valueOf(Database.users.size()+1);
        String status = "";
        if(Database.users.stream()
                .noneMatch(user -> user.getLogin().equals(newUser.getLogin()))) {
            User user1 = User.builder()
                    .id(id)
                    .login(newUser.getLogin())
                    .password(newUser.getPassword())
                    .name(newUser.getName())
                    .surname(newUser.getSurname())
                    .build();
            Database.users.add(user1);
        } else {
            status = "ErrorExist";
        }
        return status;
    }

    @Override
    public User getUserByLogin(String login) {
        return Database.users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst().get();
    }

}
