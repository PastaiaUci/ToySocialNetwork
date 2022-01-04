package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    SuperService superService;

    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button loginButton;
    @FXML
    Button registerButton;


    public void setServiceController(SuperService serviceController) {
        this.superService = serviceController;
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {

    }

    @FXML
    public void onRegisterButtonClick(ActionEvent event) {

       try {
           Node source = (Node) event.getSource();
           Stage current = (Stage) source.getScene().getWindow();
           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signup-view.fxml"));
           Parent root = fxmlLoader.load();
           SignupController signupController = fxmlLoader.getController();
           signupController.setSuperService(superService);
           Scene scene = new Scene(root, 700, 600);
           current.setTitle("Ian");
           current.setScene(scene);

       }catch (IOException e) {
           e.printStackTrace();
       }
    }
}
