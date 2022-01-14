package com.example.toysocialnetworkgui.repository.database;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.GroupMessage;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.repository.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserImagesDbRepository{
    private String url;
    private String username;
    private String password;

    public UserImagesDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void save(File file,Long id_user) {
        String sql = "INSERT INTO user_images(img_name,img,id_user) values(?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(sql);) {
            FileInputStream fis = new FileInputStream(file);
            ps.setString(1, file.getName());
            ps.setBinaryStream(2, fis, file.length());
            ps.executeUpdate();
            ps.close();
            fis.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
