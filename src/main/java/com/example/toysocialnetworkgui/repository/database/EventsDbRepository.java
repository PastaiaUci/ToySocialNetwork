package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.*;
import com.example.toysocialnetworkgui.domain.validators.EventValidator;
import com.example.toysocialnetworkgui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.*;

public class EventsDbRepository implements Repository<Long ,Event>
{
    private String url;
    private String username;
    private String password;
    private EventValidator validator;

    public EventsDbRepository(String url, String username, String password,EventValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Event findOneById(Long aLong) {
        String sql = FIND_EVENT_BY_ID_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);)
        {
            statement.setLong(1, aLong);
            List<Event> rez = getEvents(statement);
            if (rez.isEmpty())
                return null;
            return rez.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public Iterable<Event> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_EVENTS_DB);)
        {
            return getEvents(statement);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Event entity) {
        validator.validate(entity);
        String sql = SAVE_EVENT_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescriere());
            preparedStatement.setString(3, entity.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("ID can not be null!");
        String sql = DELETE_EVENT_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        String sql = UPDATE_EVENT_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setString(2,entity.getDescriere());
            preparedStatement.setString(3,entity.getDate());
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Event> getEvents(PreparedStatement preparedStatement) throws SQLException {
        List<Event> events = new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                String nume = resultSet.getString("nume");
                String descriere = resultSet.getString("descriere");
                String data =  resultSet.getString("data");
                Event event = new Event(nume, descriere,data);
                events.add(event);
            }
            return events;
        }
    }

    @Override
    public Event findOne(Long longLongTuple) {
        return null;
    }

    @Override
    public List<Event> getUserByUsername(String username){ return null; }

    @Override
    public Event findOneByOtherAttributes(List<Object> args){ return null; }

}
