package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import java.util.Date;

public class MainController {

    SuperService superService;
    private User currentUser;

    ObservableList<User> allUsers = FXCollections.observableArrayList();
    ObservableList<Friendship> allRequests = FXCollections.observableArrayList();

    @FXML
    Button sendDeleteButton;
    @FXML
    Button sendRequestButton;
    @FXML
    Button sendAcceptButton;
    @FXML
    Button sendRejectButton;
    @FXML
    Button viewFriendsButton;

    @FXML
    Label userName;

    @FXML
    TableView<User> userTableView;
    @FXML
    TableColumn<User,String> lastNameColumn;
    @FXML
    TableColumn<User,String> firstNameColumn;

    @FXML
    TableView<Friendship> friendshipTableView;

    @FXML
    TableColumn<Friendship,String> fromColumn;
    @FXML
    TableColumn<Friendship,String> toColumn;
    @FXML
    TableColumn<Friendship,String> statusColumn;
    @FXML
    TableColumn<Friendship, Date>  dateColumn;

    @FXML
    public void initialize() {

        //this.userTableView.managedProperty().bind(this.userTableView.visibleProperty());
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fr1"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("fr2"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        userTableView.setItems(allUsers);
        friendshipTableView.setItems(allRequests);
    }

        public void setServiceController(SuperService superService) {
            this.superService = superService;
        }

        public void setCurrentUser(User user) {
            this.currentUser = user;
        }

    public void afterLoad(SuperService superService, User user) {
        this.setServiceController(superService);
        this.setCurrentUser(user);

        this.updateUsers();
        userName.setText(currentUser.getLastName());
        //this.updateRequests();
    }


    public void setUsernames(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }

    public void updateUsers() {
        this.allUsers.clear();
        Iterable<User> users = this.superService.getAllUsers();
        this.setUsernames(users);
    }


    @FXML
    public void sendFriendRequest(ActionEvent actionEvent) {

    }

    @FXML
    public void onFriendsButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friends-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 600);
            current.setTitle("Ian");
            current.setScene(scene);
            FriendsListController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,superService.findUsersByName(currentUser.getFirstName()).get(0));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

