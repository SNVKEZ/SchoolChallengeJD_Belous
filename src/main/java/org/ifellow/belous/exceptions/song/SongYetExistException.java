package org.ifellow.belous.exceptions.song;

public class SongYetExistException extends RuntimeException{
    public SongYetExistException(String message) {
        super(message);
    }
}
