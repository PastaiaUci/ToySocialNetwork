package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Group;
import com.example.toysocialnetworkgui.domain.GroupMessage;
import com.example.toysocialnetworkgui.repository.database.GroupDbRepository;

import java.util.List;

public class GroupsService {
    private GroupDbRepository repo;

    public GroupsService(GroupDbRepository repo) {
        this.repo = repo;
    }

    public List<Long> getUsersIdPartOfGroup(Long id_group){
        return repo.getUsersIdPartOfGroup(id_group);
    }

    public List<GroupMessage> getGroupMessagesFromGroup(Long id_group){
        return repo.getGroupMessagesFromGroup(id_group);
    }

    public List<Group> findGroupsOfAUser(Long id_user){
        List<Group> groups = repo.findGroupsOfAUser(id_user);
        for(Group group : groups)
            group.setImage_url("group_url.png");
        return groups;
    }

    public void saveGroupMessage(GroupMessage groupMessage){
        this.repo.saveGroupMessage(groupMessage);
    }
}
