package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.UserDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;


public class UserDaoImpl implements UserDao {

    @Override
    public void create(RegisterUserDtoRequest newUser) {
        User user1 = User.builder()
                .id(String.valueOf(Database.users.size() + 1))
                .login(newUser.getLogin())
                .password(newUser.getPassword())
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .build();
        Database.users.add(user1);
    }

    @Override
    public boolean checkExistUser(String login) {
        return Database.users.stream()
                .anyMatch(user -> user.getLogin().equals(login));
    }

    @Override
    public boolean authorization(LoginDtoRequest userLogin, String login) {
        return Database.users.stream()
                .anyMatch(user -> user.getLogin().equals(login) && user.getPassword().equals(userLogin.getPassword()));
    }


    @Override
    public User getUserByLogin(String login) {
        return Database.users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst().get();
    }

    @Override
    public void recordSession(String ID) {
        Database.activeSessions.add(ID);
    }
}
