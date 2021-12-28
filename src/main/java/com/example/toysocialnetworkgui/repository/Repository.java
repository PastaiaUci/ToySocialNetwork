package com.example.toysocialnetworkgui.repository;

import com.example.toysocialnetworkgui.domain.Entity;
import com.example.toysocialnetworkgui.domain.validators.ValidationException;
import com.example.toysocialnetworkgui.repository.repoExceptions.*;

import java.util.List;
import java.util.Objects;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          null if it was not found
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E findOneById(ID id);

    /**
     *
     * @param args
     *          list of attributes by which the search is done
     * @return
     *
     */
    E findOneByOtherAttributes(List<Object> args);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    void save(E entity);


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @throws IllegalArgumentException
     *                   if the given id is null.
     * @throws EntityNotFoundError
     *                   if the entity with the given id does not exist
     */
    void delete(ID id);

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     * @throws EntityNotFoundError
     *              if the entity to be updated was not found
     */
    void update(E entity);

}

