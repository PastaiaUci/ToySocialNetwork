package com.example.toysocialnetworkgui.domain.validators;

public interface Validator<T> {

    /**
     * validates an entity
     * @param entity
     *          entity must not be null
     * @throws ValidationException
     *          if the entity is invalid
     */
    void validate(T entity) throws ValidationException;
}