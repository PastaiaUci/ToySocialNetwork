package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.TEMPORARY_USER_ID;

public class UserService {
    private Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    public void addUser(User user) {
        user.setId(TEMPORARY_USER_ID);
        repo.save(user);
    }

    public void removeUser(Long id){
        repo.delete(id);
    }

    public Iterable<User> findAll(){
        return repo.findAll();
    }

    public User findUserByID(Long id){
        return repo.findOneById(id);
    }

    public User findUserByFirstAndLastName(String first_name, String last_name) {
        List<Object> args = new ArrayList<>();
        args.add(first_name);
        args.add(last_name);
        return repo.findOneByOtherAttributes(args);
    }
}
