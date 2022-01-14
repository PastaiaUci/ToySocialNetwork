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

public class FriendsListController {
    SuperService superService;
    private User currentUser;
    @FXML
    TableView<User> friendsTableView;
    @FXML
    TableColumn<User,String> lastNameColumn;
    @FXML
    TableColumn<User,String> firstNameColumn;
    @FXML
    Label friendsLabel;
    ObservableList<User> allFriends = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        friendsTableView.setItems(allFriends);
    }

    public void setServiceController(SuperService superService) {
        this.superService = superService;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void updateAllFriends(){
        this.allFriends.clear();
        Iterable<User> friends = this.superService.getAllFriendsForGivenUser(currentUser);
        this.setUsernames(friends);
    }

    public void setUsernames(Iterable<User> users) {
        users.forEach( u -> this.allFriends.add(u));
    }

    public void afterLoad(SuperService superService, User user) {
        this.setServiceController(superService);
        this.setCurrentUser(user);

        this.updateAllFriends();
        friendsLabel.setText("Friends of: " + currentUser.getLastName());
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 896, 578);
            current.setTitle("Ian");
            current.setScene(scene);
            Main2Controller mainController = fxmlLoader.getController();
            mainController.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteFriendClick(ActionEvent actionEvent) {
            if (friendsTableView.getSelectionModel().getSelectedItem() == null)
                return;
            Long id_friend = friendsTableView.getSelectionModel().getSelectedItem().getId();
            this.superService.deleteFriendForUser(currentUser, superService.findUserById(id_friend));
            this.updateAllFriends();
    }
}
