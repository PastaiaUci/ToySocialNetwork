package com.example.toysocialnetworkgui.repository.repoExceptions;

public class DuplicatedIDError extends RepoException{
    public DuplicatedIDError() {
        super();
    }

    public DuplicatedIDError(String message) {
        super(message);
    }

    public DuplicatedIDError(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedIDError(Throwable cause) {
        super(cause);
    }

    protected DuplicatedIDError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return "|| DuplicatedIDError || -->: " + super.getMessage();
    }
}
