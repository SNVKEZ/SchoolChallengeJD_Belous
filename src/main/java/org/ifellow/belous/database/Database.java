package org.ifellow.belous.database;

import org.ifellow.belous.Server;
import org.ifellow.belous.model.Comment;
import org.ifellow.belous.model.Song;
import org.ifellow.belous.model.User;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    public static final List<User> users = new LinkedList<>();
    public static final List<Song> songs = new LinkedList<>();
    public static final List<Comment> comments = new LinkedList<>();
    public static final Map<String,String> activeSessions = new HashMap<>();

    private Database(){}

    private static class DatabaseState implements Serializable {
        private static final long serialVersionUID = 1L;

        List<User> users;
        List<Song> songs;
        List<Comment> comments;
        Map<String, String> activeSessions;

        public DatabaseState(List<User> users, List<Song> songs, List<Comment> comments, Map<String, String> activeSessions) {
            this.users = users;
            this.songs = songs;
            this.comments = comments;
            this.activeSessions = activeSessions;
        }
    }

    // Метод для сохранения данных в файл
    public static void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            DatabaseState state = new DatabaseState(users, songs, comments, activeSessions);
            oos.writeObject(state);
        }
    }

    // Метод для загрузки данных из файла
    public static void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()){
            file.createNewFile();
            LOGGER.log(Level.INFO, "Database file missing, new one created");
            return;
        }
        if (file.length() == 0) {
            LOGGER.log(Level.INFO, "The database is empty, but everything works");
            return; // Если файл пустой, ничего не загружаем
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            DatabaseState state = (DatabaseState) ois.readObject();
            users.clear();
            users.addAll(state.users);
            songs.clear();
            songs.addAll(state.songs);
            comments.clear();
            comments.addAll(state.comments);
            activeSessions.clear();
            activeSessions.putAll(state.activeSessions);
            LOGGER.log(Level.INFO, "Database file successfully readed");
        }
    }
}