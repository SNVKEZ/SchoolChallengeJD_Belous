package org.ifellow.belous.daoimpl;

import org.ifellow.belous.dao.UserDao;
import org.ifellow.belous.database.Database;
import org.ifellow.belous.dto.request.LoginDtoRequest;
import org.ifellow.belous.dto.request.RegisterUserDtoRequest;
import org.ifellow.belous.model.User;

import java.util.Iterator;
import java.util.Map;


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

    public String getLoginByToken(String token) {
        return Database.activeSessions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void logOutUser(String token) {
        String login = null;
        Iterator<Map.Entry<String, String>> iterator = Database.activeSessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue().equals(token)) {
                login = entry.getKey(); // Удаляем запись
            }
        }
        Database.activeSessions.remove(login);
    }

    @Override
    public boolean checkActiveSession(String login) {
        return Database.activeSessions.containsKey(login);
    }

    @Override
    public boolean checkActiveSessionByToken(String token) {
        return Database.activeSessions.containsValue(token);
    }

    @Override
    public void recordSession(String login, String ID) {
        Database.activeSessions.put(login,ID);
    }

    @Override
    public void deleteUser(String login) {
        Database.users.removeIf(user -> user.getLogin().equals(login));
    }
}
