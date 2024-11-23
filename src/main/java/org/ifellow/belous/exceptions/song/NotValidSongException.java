package org.ifellow.belous.exceptions.song;

public class NotValidSongException extends RuntimeException{
    public NotValidSongException(String message, String field) {
        super(message + field);
    }
}
