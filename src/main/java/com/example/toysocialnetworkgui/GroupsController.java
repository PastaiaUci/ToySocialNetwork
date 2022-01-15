package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.Observer.Observer;
import com.example.toysocialnetworkgui.domain.Group;
import com.example.toysocialnetworkgui.domain.GroupMessage;
import com.example.toysocialnetworkgui.domain.Message;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.ACTIVE_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.SIMPLE_MESSAGE;

public class GroupsController implements Observer {
    protected SuperService superService;
    private User currentUser;
    private final ObservableList<GroupMessage> groupMessages = FXCollections.observableArrayList();
    private final ObservableList<Group> groups = FXCollections.observableArrayList();

    @FXML
    TextField searchTextField;
    @FXML
    TextArea textArea;
    @FXML
    ListView<Group> groupsListView;
    @FXML
    ListView<GroupMessage> discussionListView;

    @FXML
    Button backButton;

    @FXML
    public void initialize() {
        searchTextField.setPromptText("Search");
        this.groupsListView.setCellFactory(param -> new GroupListViewCell(currentUser.getId()));
        this.discussionListView.setCellFactory(param->new DisscussionListViewCell(currentUser.getId(),superService));
        this.groupsListView.setItems(groups);
        this.discussionListView.setItems(groupMessages);
    }

    public void setServiceController(SuperService superService){
        this.superService = superService;
    }



    public void back(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Pixel");
            current.setScene(scene);
            Main2Controller ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessageButtonClick(ActionEvent actionEvent) {
        String mesaj = textArea.getText();
        if(mesaj.strip().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't send message!");
            alert.setHeaderText("Can't send message!");
            alert.showAndWait();
            return;
        }
        Group group = groupsListView.getSelectionModel().getSelectedItem();
        if(group == null)
            return;
        this.superService.saveGroupMessage(currentUser.getId(), group.getId(),mesaj);
        this.textArea.clear();
        //updateGroupMessages();
    }


    public void showButtonClick(ActionEvent actionEvent) {
        updateGroupMessages();
    }
    static final class DisscussionListViewCell extends ListCell<GroupMessage>{
        private final Long idCurrentUser;
        private final SuperService superService;

        public DisscussionListViewCell(Long userid,SuperService superService){
            this.idCurrentUser = userid;
            this.superService = superService;
        }

        @Override
        public void updateItem(GroupMessage item, boolean empty){
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
                Label messageLabel = styleLabel(item.getMesaj());
                Label nameLabel = new Label();
                nameLabel.setText("--"+superService.findUserById(item.getId__user_from()).getLastName()+"--");
                nameLabel.setMinHeight(50);
                nameLabel.setMinWidth(50);
                if(item.getId__user_from().equals(idCurrentUser)) {
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    nameLabel.setAlignment(Pos.CENTER_RIGHT);
                    hBox.getChildren().addAll(nameLabel,messageLabel,imageView);
                }
                else{
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.getChildren().addAll(imageView,messageLabel,nameLabel);
                }
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



    static final class  GroupListViewCell extends ListCell<Group> {
        private final Long idCurrentUser;

        public GroupListViewCell(Long userId) {
            this.idCurrentUser = userId;
        }

        @Override
        protected void updateItem(Group item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Image image = new Image("com/example/toysocialnetworkgui/Images/group_url.png");
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                Label groupNameLabel =styleLabel(item.getName());
                HBox hBox = new HBox();
                hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL,null,null)));
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(imageView,groupNameLabel);
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
        this.superService.addObserver(this);
        this.updateGroupMessages();
        this.updateGroups();
    }

    @Override
    public void updateFriends() {

    }

    @Override
    public void updateRequests() {

    }

    @Override
    public void updateEvents() {

    }

    @Override
    public void updateUsers() {

    }

    @Override
    public void updateMessages() {

    }

    @Override
    public void updateGroupMessages() {
        this.groupMessages.clear();
        Group group = groupsListView.getSelectionModel().getSelectedItem();
        if(group == null)
            return;
       List<GroupMessage> groupMessagess = this.superService.getGroupMessagesFromGroup(group.getId());
       this.setGroupMessages(groupMessagess);
    }

    public void updateGroups(){
        this.groups.clear();
        Iterable<Group> groupss = this.superService.findGroupsOfAUser(currentUser.getId());
        this.setGroups(groupss);
    }

    /*public void updateUsersAfterSearch(String first_name){
        this.allUsers.clear();
        Iterable<User> users = this.superService.findUsersByName(first_name);
        this.setUsernames(users);
    }*/

    public void setGroupMessages(Iterable<GroupMessage> groupMessagess){
        groupMessagess.forEach(u->this.groupMessages.add(u));
    }

    public void setGroups(Iterable<Group> groupss) {
        groupss.forEach( u -> this.groups.add(u));
    }
}
