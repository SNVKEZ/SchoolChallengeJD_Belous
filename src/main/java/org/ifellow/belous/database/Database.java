package org.ifellow.belous.database;

import org.ifellow.belous.model.Song;
import org.ifellow.belous.model.User;

import java.util.LinkedList;
import java.util.List;

public class Database {
    public static final List<User> users = new LinkedList<>();
    public static final List<Song> songs = new LinkedList<>();

    private Database(){}
}
