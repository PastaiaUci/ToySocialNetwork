package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Group;
import com.example.toysocialnetworkgui.domain.GroupMessage;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FriendsListController {
    protected SuperService superService;
    private User currentUser;
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private final ObservableList<User> allFriends = FXCollections.observableArrayList();


    @FXML
    ListView<User> allUsersListView;
    @FXML
    ListView<User> allFriendsListView;

    @FXML
    public void initialize() {

        this.allUsersListView.setCellFactory(param -> new ListViewCell(currentUser));
        this.allFriendsListView.setCellFactory(param->new ListViewCell(currentUser));
        this.allUsersListView.setItems(allUsers);
        this.allFriendsListView.setItems(allFriends);
    }

    public void setServiceController(SuperService superService){
        this.superService = superService;
    }

    public void findButtonCLick(ActionEvent actionEvent) {
    }

    public void refreshButtonCLick(ActionEvent actionEvent) {
    }

    public void backButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 896, 578);
            current.setTitle("Amuly");
            current.setScene(scene);
            Main2Controller ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(ActionEvent actionEvent) {
    }


    static final class ListViewCell extends ListCell<User> {
        private final User CurrentUser;

        public ListViewCell(User user){
            this.CurrentUser = user;
        }

        @Override
        public void updateItem(User item, boolean empty){
            super.updateItem(item,empty);
            if(empty){
                setGraphic(null);
            }
            else{
                HBox hBox = new HBox();
                Image image = new Image("com/example/toysocialnetworkgui/Images/group_url.png");
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                Label nameLabel = styleLabel(item.getFirstName()+" "+item.getLastName());
                hBox.setAlignment(Pos.CENTER_LEFT);
                nameLabel.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(imageView,nameLabel);
                hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL,null,null)));
                setGraphic(hBox);
            }
        }

        private Label styleLabel(String msg){
            var label=new Label(msg);
            label.setMinWidth(50);
            label.setMinHeight(50);
            label.setStyle("-fx-hgap: 10px;" +
                    "    -fx-padding: 20px;" +
                    "" +
                    "    -fx-background-color: #997dff;" +
                    "    -fx-background-radius: 25px;" +
                    "" +
                    "    -fx-border-radius: 25px;" +
                    "    -fx-border-width: 5px;" +
                    "    -fx-border-color: #997dff;" +
                    "-fx-text-fill: white;"+
                    "-fx-font-size: 25px");
            return label;
        }

    }

    public void afterLoad(SuperService superService, User currentUser) {
        this.superService = superService;
        this.currentUser = currentUser;
        //this.superService.addObserver(this);
        this.updateUsers();
        this.updateFriends();
    }

    private void updateUsers() {
        this.allUsers.clear();
        Iterable<User> allUserss = this.superService.getAllUsers();
        this.setUsers(allUserss);
    }

    public void updateFriends(){
        this.allFriends.clear();
        Iterable<User> allFriends = this.superService.getAllFriendsForGivenUser(currentUser);
        this.setFriends(allFriends);
    }

    public void setFriends(Iterable<User> friendss){
        friendss.forEach(u->this.allFriends.add(u));
    }

    public void setUsers(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }
}
