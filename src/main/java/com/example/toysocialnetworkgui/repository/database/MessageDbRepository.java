package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.validators.MessageValidator;
import com.example.toysocialnetworkgui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.ACTIVE_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.DELETED_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.*;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private MessageValidator validator;

    public MessageDbRepository(String url, String username, String password, MessageValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOneById(Long aLong) {
        String sql = FIND_MESSAGE_BY_ID_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,aLong);
            List<Message> message_found = getMessages(preparedStatement);
            if(message_found.isEmpty())
                return null;
            return message_found.get(0);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Message findOneByOtherAttributes(List<Object> args) {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_MESSAGES_DB);)
        {
            return getMessages(preparedStatement);

        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Message entity) {
        validator.validate(entity);
        String sql = SAVE_MESSAGE_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,entity.getIdFrom());
            preparedStatement.setLong(2,entity.getIdTo());
            preparedStatement.setString(3,entity.getMesaj());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(entity.getDataTrimitere()));
            preparedStatement.setLong(5,entity.getIdReply());
            preparedStatement.setString(6,entity.getDeleteStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = DELETE_MESSAGE_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,DELETED_MESSAGE);
            preparedStatement.setLong(2,aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Message entity) {
       String sql = UPDATE_MESSAGE_DB;
       try(Connection connection = DriverManager.getConnection(url,username,password);
           PreparedStatement preparedStatement = connection.prepareStatement(sql))
       {
          preparedStatement.setString(1,entity.getDeleteStatus());
          preparedStatement.setLong(2,entity.getId());
          preparedStatement.executeUpdate();
       } catch (SQLException ex){
           ex.printStackTrace();
       }
    }

    private List<Message> getMessages(PreparedStatement preparedStatement) throws SQLException {
        List<Message> messages = new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                Long id_mesaj = resultSet.getLong("id_mesaj");
                Long id_from = resultSet.getLong("id_from");
                Long id_to = resultSet.getLong("id_to");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime data_trimitere = resultSet.getTimestamp("data_trimitere").toLocalDateTime();
                Long id_reply = resultSet.getLong("id_reply");
                String delete_status = resultSet.getString("delete_status");
                Message message = new Message(id_from,id_to,mesaj,data_trimitere,id_reply,delete_status);
                message.setId(id_mesaj);
                messages.add(message);
            }
            return messages;
        }
    }

    @Override
    public Message findOne(Long longLongTuple) {
        return null;
    }

    @Override
    public List<Message> getUserByUsername(String username){ return null; }

    @Override
    public void subscribe(Long user_id, Long event_id) {

    }

    @Override
    public void unsubscribe(Long user_id, Long event_id) {
    }

    @Override
    public Iterable<Message> getAllEventsForUser(Long aLong) {
        return null;
    }
}
