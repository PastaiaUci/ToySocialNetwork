package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.Group;
import com.example.toysocialnetworkgui.domain.GroupMessage;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.SAVE_MESSAGE_DB;

public class GroupDbRepository{
    private String url;
    private String username;
    private String password;

    public GroupDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public List<Group> findGroupsOfAUser(Long id_user){
        String sql = "SELECT G.id_group, G.name FROM groups G\n" +
                "INNER JOIN group_members GM ON G.id_group=GM.id_group\n" +
                "where GM.id_membru= ?";
        List<Group> groups = new ArrayList<Group>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,id_user);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long id_group = resultSet.getLong("id_group");
                String name = resultSet.getString("name");
                Group group = new Group(name);
                group.setId(id_group);
                groups.add(group);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return groups;
    }

    public List<Long> getUsersIdPartOfGroup(Long id_group){
        String sql = "Select id_membru from group_members where id_group = ?";
        List<Long> users_id = new ArrayList<Long>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,id_group);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long id_membru = resultSet.getLong("id_membru");
                users_id.add(id_membru);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return users_id;
    }

    public List<GroupMessage> getGroupMessagesFromGroup(Long id_group){
        String sql = "Select * from group_messages where id_group_to = ?";
        List<GroupMessage> groupMessages = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,id_group);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long id_group_message = resultSet.getLong("id_group");
                Long id_user_from = resultSet.getLong("id_user_from");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime data_trimitere = resultSet.getTimestamp("data_trimitere").toLocalDateTime();
                Long id_reply = resultSet.getLong("id_reply");
                String delete_status = resultSet.getString("delete_status");
                GroupMessage message = new GroupMessage(id_user_from,id_group,mesaj,data_trimitere,id_reply,delete_status);
                message.setId(id_group_message);
                groupMessages.add(message);

            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return groupMessages;
    }

    public void saveGroupMessage(GroupMessage groupMessage) {
        String sql = "insert into group_messages(id_user_from,id_group_to,mesaj,data_trimitere,id_reply,delete_status)"+
                " values(?,?,?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setLong(1,groupMessage.getId__user_from());
            preparedStatement.setLong(2,groupMessage.getId_group_to());
            preparedStatement.setString(3,groupMessage.getMesaj());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(groupMessage.getData_trimitere()));
            preparedStatement.setLong(5,groupMessage.getId_reply());
            preparedStatement.setString(6,groupMessage.getDelete_status());
            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
