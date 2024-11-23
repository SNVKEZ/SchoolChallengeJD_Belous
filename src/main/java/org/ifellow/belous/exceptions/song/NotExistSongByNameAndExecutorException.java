package org.ifellow.belous.exceptions.song;

public class NotExistSongByNameAndExecutorException extends RuntimeException{
    public NotExistSongByNameAndExecutorException(String message) {
        super(message);
    }
}
