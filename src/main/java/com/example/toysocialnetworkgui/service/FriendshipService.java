package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService {
    public Repository<Tuple<Long, Long>, Friendship> repo;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> repo) {
        this.repo = repo;
    }

   /* public void addFriendShip(Friendship friendship) {
        repo.save(friendship);
    }*/

    public void deleteFriendship(Tuple<Long, Long> id) {
        Tuple<Long, Long> original_id = id;
        Tuple<Long, Long> reversed_id = new Tuple<Long, Long>(original_id.getRight(), original_id.getLeft());
        Friendship original_friendship = repo.findOne(original_id);
        Friendship reveresed_friendship = repo.findOne(reversed_id);
        if (original_friendship == null && reveresed_friendship == null)
            return;
        if (original_friendship == null) {
            repo.delete(reversed_id);
        }
        if (reveresed_friendship == null)
            repo.delete(original_id);
    }

    public Iterable<Friendship> findAll() {
        return repo.findAll();
    }


}
