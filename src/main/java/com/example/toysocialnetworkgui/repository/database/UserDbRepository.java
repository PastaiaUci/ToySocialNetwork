package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.domain.validators.UserValidator;
import com.example.toysocialnetworkgui.repository.Repository;
import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.*;
import java.sql.*;
import java.util.*;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private UserValidator validator;

    public UserDbRepository(String url, String username, String password, UserValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOneById(Long aLong) {
        String sql = FIND_USER_BY_ID_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);)
        {
            statement.setLong(1, aLong);
            List<User> rez = getUsers(statement);
            if (rez.isEmpty())
                return null;
            return rez.get(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public User findOneByOtherAttributes(List<Object> args){
        if(args == null)
            throw new IllegalArgumentException("List of attributes must not be null!");
        String first_name = args.get(0).toString();
        String last_name = args.get(1).toString();
        String sql = FIND_USER_BY_FIRST_AND_LAST_NAME;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,first_name);
            preparedStatement.setString(2,last_name);
            List<User> rez = getUsers(preparedStatement);
            if (rez.isEmpty())
                return null;
            return rez.get(0);
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS_DB);)
        {
            return getUsers(statement);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User entity) {
        validator.validate(entity);
        String sql = SAVE_USER_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("ID can not be null!");
        String sql = DELETE_USER_DB;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        String sql = UPDATE_USER_DB;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1,entity.getFirstName());
            preparedStatement.setString(2,entity.getLastName());
            preparedStatement.setLong(3,entity.getId());
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<User> getUsers(PreparedStatement preparedStatement) throws SQLException {
        List<User> users = new ArrayList<>();
        try(ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password =  resultSet.getString("password");
                User user = new User(firstName, lastName,password);
                user.setId(id);
                users.add(user);
            }
            return users;
        }
    }

    @Override
    public User findOne(Long id) {
        String sql="SELECT * from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password))
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next())
                return null;
            User user = new User(resultSet.getString("first_name"), resultSet.getString("last_name"),resultSet.getString("password"));
            user.setId(resultSet.getLong("id"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //new add
   @Override
    public List<User> getUserByUsername(String username_1){
       String sql = FIND_USER_BY_USERNAME_DB;
       try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);)
       {
           statement.setString(1, username_1);
           List<User> rez = getUsers(statement);
           if (rez.isEmpty())
               return null;
           return rez;
       } catch (SQLException ex) {
           ex.printStackTrace();
       }
       return null;
   }

}

