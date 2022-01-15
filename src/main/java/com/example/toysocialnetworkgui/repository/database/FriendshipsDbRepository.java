
package com.example.toysocialnetworkgui.repository.database;
import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.validators.FriendshipValidator;
import com.example.toysocialnetworkgui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.*;


public class FriendshipsDbRepository implements Repository<Tuple<Long,Long>, Friendship> {

    private String url;
    private String username;
    private String password;
    private FriendshipValidator validator;

    public FriendshipsDbRepository(String url, String username, String password, FriendshipValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOneById(Tuple<Long,Long> id) {
        String sql = FIND_FRIENDSHIP_BY_BOTH_ID_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);)
        {
            statement.setLong(1, id.getLeft());
            statement.setLong(2,id.getRight());
            List<Friendship> rez = getFriendShips(statement);
            if (rez.isEmpty())
                return null;
            return rez.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //de implementat daca e nevoie
    @Override
    public Friendship findOneByOtherAttributes(List<Object> args) {
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id_1");
                Long id2 = resultSet.getLong("id_2");
                String date = resultSet.getString("data_crearii");
                String friendshipStatus = resultSet.getString("friendship_status");
                Long sender = resultSet.getLong("sender");
                Friendship friendship = new Friendship(id1,id2,date,friendshipStatus,sender);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public void save(Friendship entity) {
        validator.validate(entity);
        String sql = SAVE_FRIENDSHIP_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1, entity.getId().getLeft());
            preparedStatement.setLong(2, entity.getId().getRight());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(entity.getDate()));
            preparedStatement.setString(4, entity.getFriendshipStatus());
            preparedStatement.setLong(5, entity.getSender());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Tuple<Long, Long> id) {
        if(id == null)
            throw new IllegalArgumentException("ID can not be null!");
        String sql = DELETE_FRIENDSHIP_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, id.getLeft());
            ps.setLong(2, id.getRight());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Friendship entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        String sql = "UPDATE friendships SET friendship_status='" + entity.getFriendshipStatus() + "', data_crearii='" + entity.getDate() +"' WHERE id_1='" + entity.getId().getLeft() + "' and id_2='" + entity.getId().getRight() + "' ";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {

            preparedStatement.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Friendship> getFriendShips(PreparedStatement preparedStatement) throws SQLException {
        List<Friendship> friendships = new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                Long id_1 = resultSet.getLong("id_1");
                Long id_2 = resultSet.getLong("id_2");
                //String date_of_creation = resultSet.getString("date_of_creation");
                LocalDateTime date_of_creation = resultSet.getTimestamp("data_crearii").toLocalDateTime();
                Friendship friendship = new Friendship(date_of_creation);
                Tuple<Long,Long> friendship_id = new Tuple<>(id_1,id_2);
                friendship.setId(friendship_id);
                friendships.add(friendship);
            }
            return friendships;
        }
    }





    public Friendship findOne(Tuple<Long,Long> tuple) {
        String sql="SELECT * from friendships WHERE id_1 = ? and id_2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password))

        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, tuple.getLeft());
            statement.setLong(2,tuple.getRight());
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next())
                return null;

            Friendship friendship = new Friendship(resultSet.getLong("id_1"), resultSet.getLong("id_2"), resultSet.getString("data_crearii"), resultSet.getString("friendship_status"), resultSet.getLong("sender"));
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Friendship> getUserByUsername(String username){ return null; }

    @Override
    public void subscribe(Tuple<Long, Long> user_id, Tuple<Long, Long> event_id) {

    }

    @Override
    public void unsubscribe(Tuple<Long, Long> user_id, Tuple<Long, Long> event_id) {

    }

    @Override
    public Iterable<Friendship> getAllEventsForUser(Tuple<Long, Long> longLongTuple) {
        return null;
    }

    @Override
    public void turnOffNotifications(Tuple<Long, Long> longLongTuple, Tuple<Long, Long> id1) {

    }

    @Override
    public void turnOnNotifications(Tuple<Long, Long> longLongTuple, Tuple<Long, Long> id1) {

    }
}

