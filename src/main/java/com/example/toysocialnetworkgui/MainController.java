package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.ServiceException;
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


        fromColumn.setCellValueFactory( param -> {
            User user = this.superService.findUserById(param.getValue().getFr1());
            ReadOnlyObjectWrapper<String> str = new ReadOnlyObjectWrapper<>();
            if(user != null)
                str.set(user.getLastName());
            return str;
        });
        toColumn.setCellValueFactory( param -> {
            User user = this.superService.findUserById(param.getValue().getFr2());
            ReadOnlyObjectWrapper<String> str = new ReadOnlyObjectWrapper<>();
            if(user != null)
                str.set(user.getLastName());
            return str;
        });
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


        this.updateRequests();
    }


    public void updateRequests(){
        this.allRequests.clear();
        Iterable<Friendship> friendships = this.superService.allRequestsOfAUser(currentUser.getId());
        this.setFriendships(friendships);
    }
    public void setUsernames(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }
    public void setFriendships(Iterable<Friendship> friendships) {
        friendships.forEach( u -> this.allRequests.add(u));
    }

    public void updateUsers() {
        this.allUsers.clear();
        Iterable<User> users = this.superService.getAllUsers();
        this.setUsernames(users);
    }

    @FXML
    public void sendRequest() {

        try {
            if (userTableView.getSelectionModel().getSelectedItem() == null)
                return;
            this.superService.sendFriendRequest(currentUser.getId(),userTableView.getSelectionModel().getSelectedItem().getId());
            this.updateRequests();
        }
        catch (ServiceException e){
           System.out.println("stefaneeee");
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Can't send friend request!");
           alert.setHeaderText("Mi ai dat leanul pe jos!");
           alert.showAndWait();

        }
    }

    @FXML
    public void acceptRequest() {
        String response = "approved";
        try {
            if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
                return;
            Long id_receiver;
            if (friendshipTableView.getSelectionModel().getSelectedItem().getFr1() == currentUser.getId())
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr2();
            else
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr1();

            Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();

            if (currentUser.getId() != friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  ) {
                this.superService.responseToFriendRequest(currentUser.getId(), id_receiver, response);
                this.updateRequests();
            }
        }

        catch (ServiceException e){
            e.printStackTrace();
        }
    }



    @FXML
    public void rejectRequest() {
        String response = "rejected";
        try {
            if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
                return;
            Long id_receiver;
            if(friendshipTableView.getSelectionModel().getSelectedItem().getFr1() == currentUser.getId())
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr2();
            else
                id_receiver = friendshipTableView.getSelectionModel().getSelectedItem().getFr1();
            Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
            if (currentUser.getId() != friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  )
            {
            this.superService.responseToFriendRequest(currentUser.getId(),id_receiver,response);
            this.updateRequests();
            }
        }
        catch (ServiceException e){
            e.printStackTrace();
        }
    }


}

