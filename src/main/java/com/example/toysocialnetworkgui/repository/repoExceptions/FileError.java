package com.example.toysocialnetworkgui.repository.repoExceptions;

public class FileError extends RepoException{
    public FileError() {
        super();
    }

    public FileError(String message) {
        super(message);
    }

    public FileError(String message, Throwable cause) {
        super(message, cause);
    }

    public FileError(Throwable cause) {
        super(cause);
    }

    protected FileError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return "FileError: " + super.getMessage();
    }
}
