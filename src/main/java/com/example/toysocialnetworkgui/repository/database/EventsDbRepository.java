package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.*;
import com.example.toysocialnetworkgui.domain.validators.EventValidator;
import com.example.toysocialnetworkgui.repository.Repository;
import javafx.scene.control.Alert;

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
            preparedStatement.setTimestamp(3,Timestamp.valueOf(entity.getDate()));
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
            preparedStatement.setTimestamp(3,Timestamp.valueOf(entity.getDate()));
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Iterable<Event> getAllEventsForUser(Long id) {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(EVENTS_USER_SUBBED_T0);)
        {
                statement.setLong(1,id);
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()) {

                        Long event_id = resultSet.getLong("event_id");

                        Event event  = this.findOneById(event_id);
                        events.add(event);
                    }
                }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return events;
    }

    public  List<Event> getEvents(PreparedStatement preparedStatement) throws SQLException {
        List<Event> events = new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                String nume = resultSet.getString("nume");
                String descriere = resultSet.getString("descriere");
                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                Event event = new Event(nume, descriere,data);
                event.setId(resultSet.getLong("id"));
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

    public  void subscribe(Long user_id,Long event_id){

        String sql = SUB_TO_EVENT_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2,event_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Can not double sub!\n");
            alert.showAndWait();
            return;
        }
    }

    public void turnOffNotifications(Long id1,Long id2){
        String sql = TURN_OFF_NOTIFICATIONS;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, id1);
            preparedStatement.setLong(2,id2);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Notifications turned off for event!\n");
            alert.showAndWait();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void turnOnNotifications(Long id1,Long id2){
        String sql = TURN_ON_NOTIFICATIONS;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, id1);
            preparedStatement.setLong(2,id2);
            preparedStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Notifications turned on for event!\n");
            alert.showAndWait();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void unsubscribe(Long user_id,Long event_id){

        String sql = UNSUB_TO_EVENT_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, user_id);
            preparedStatement.setLong(2,event_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
