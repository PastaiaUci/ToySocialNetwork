
package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.domain.validators.FriendshipValidator;
import com.example.toysocialnetworkgui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


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
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_FRIENDSHIP_DB);)
        {
            return getFriendShips(statement);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
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
        String sql = UPDATE_FRIENDSHIP_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setTimestamp(1,Timestamp.valueOf(entity.getDate()));
            preparedStatement.setLong(2, entity.getId().getLeft());
            preparedStatement.setLong(3, entity.getId().getRight());
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
                LocalDateTime date_of_creation = resultSet.getTimestamp("date_of_creation").toLocalDateTime();
                Friendship friendship = new Friendship(date_of_creation);
                Tuple<Long,Long> friendship_id = new Tuple<>(id_1,id_2);
                friendship.setId(friendship_id);
                friendships.add(friendship);
            }
            return friendships;
        }
    }


    /*public List<Friendship> getAllFriendsForGivenUser(Long id_user){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement1 = connection.prepareStatement(FIND_ALL_FRIENDS_FOR_GIVEN_USER_RIGHT_TO_LEFT_DB);
             PreparedStatement statement2 = connection.prepareStatement(FIND_ALL_FRIENDS_FOR_GIVEN_USER_RIGHT_TO_LEFT_DB))
        {
            return getFriendShips(statement);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }*/
}

