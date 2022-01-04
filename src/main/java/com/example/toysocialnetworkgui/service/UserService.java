package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.TEMPORARY_USER_ID;

public class UserService {
    public Repository<Long, User> repo;

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


   //new add
   public List<User> getUsersWithUsername(String username){
       List<User> users = repo.getUserByUsername(username);
       return users;
   }
}
