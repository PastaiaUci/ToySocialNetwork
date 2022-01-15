package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService {
    private Repository<Long, Message> repo;

    public MessageService(Repository<Long,Message> repo){
        this.repo = repo;
    }

    public void addMessage(Message message){
        repo.save(message);
    }

    public void deleteMessage(Long id){
        repo.delete(id);
    }

    public void undoMessageDelete(Message message){
        repo.update(message);
    }

    public Iterable<Message> findAll(){
        return repo.findAll();
    }

    public Message findMessageById(Long id){
        return repo.findOneById(id);
    }
    public List<Message> findAllSentMassagesToUsers(Long id_user_from){
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(x->(x.getIdFrom().equals(id_user_from)))
                .collect(Collectors.toList());
    }

    public List<Message> findAllReceivedMassagesFromUsers(Long id_to){
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(x->(x.getIdTo().equals(id_to)))
                .collect(Collectors.toList());
    }

}
