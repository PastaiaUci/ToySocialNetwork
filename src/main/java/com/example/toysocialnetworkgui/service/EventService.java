package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Event;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.repository.Repository;

import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.TEMPORARY_USER_ID;

public class EventService {

    public Repository<Long, Event> repo;
    public EventService(Repository<Long, Event> repo) {
        this.repo = repo;
    }

    public Iterable<Event> findAll(){
        return repo.findAll();
    }
    public void addEvent(Event event) {
        repo.save(event);
    }
    public  void  subscribe(Long user_id,Long event_id){
        this.repo.subscribe(user_id,event_id);
    }
    public  void  unsubscribe(Long user_id,Long event_id){
        this.repo.unsubscribe(user_id,event_id);
    }

    public Iterable<Event> getAllEventsForUser(Long id){
        return repo.getAllEventsForUser(id);
    }

    public void turnOffNotifications(Long id, Long id1) {
        repo.turnOffNotifications(id,id1);
    }

    public void turnOnNotifications(Long id, Long id1) {
        repo.turnOnNotifications(id,id1);
    }
}
