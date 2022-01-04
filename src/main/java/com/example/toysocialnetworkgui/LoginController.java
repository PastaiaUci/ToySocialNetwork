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
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if(username.equals("")){
            return;
        }
        /*try {
            User user = superService.findUserByUsername(username);
            System.out.println("Logging in as: " + user);
            Node source = (Node) event.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1024, 768);
            current.setTitle("Metanauts - " + user.getUsername());
            current.setScene(scene);
            MainController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(this.serviceController, user);
        } catch(RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("This user doesn't exist!\n");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void onRegisterButtonClick(ActionEvent actionEvent) {
    }
}
