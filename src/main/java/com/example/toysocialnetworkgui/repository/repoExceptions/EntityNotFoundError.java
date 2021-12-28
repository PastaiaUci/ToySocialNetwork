package com.example.toysocialnetworkgui.repository.repoExceptions;

public class EntityNotFoundError extends RepoException{
    public EntityNotFoundError() {
        super();
    }

    public EntityNotFoundError(String message) {
        super(message);
    }

    public EntityNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundError(Throwable cause) {
        super(cause);
    }

    protected EntityNotFoundError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return "|| EntityError || -->> : " + super.getMessage();
    }
}
