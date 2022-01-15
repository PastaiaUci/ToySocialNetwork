package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.Observer.Observer;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Tuple;
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
import javafx.scene.control.ListView.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.ACTIVE_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.SIMPLE_MESSAGE;

public class ChatController implements Observer {

    protected SuperService superService;
    private User currentUser, destination;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();

    @FXML
    Button back;
    @FXML
    Label destinationLabel;
    @FXML
    ListView<Message> messagesView;
    @FXML
    TextArea textarea;
    @FXML
    Button reset;
    @FXML
    Button send;
    @FXML
    TextField searchTextField;

    @FXML
    TableView<User> usersTableView;
    @FXML
    TableColumn<User,String> lastNameColumn;
    @FXML
    TableColumn<User,String> firstNameColumn;

    @FXML
    public void initialize() {
        searchTextField.setPromptText("Search");
        this.messagesView.setCellFactory(param -> new ListViewCell(currentUser.getId()));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.messagesView.setItems(messages);
        this.usersTableView.setItems(allUsers);
    }


    public void resetReply(ActionEvent actionEvent) {

    }

    public void sendMessageButtonClick(ActionEvent actionEvent) {
        Message selected = messagesView.getSelectionModel().getSelectedItem();
        User destination = usersTableView.getSelectionModel().getSelectedItem();
        if(destination.getId().equals(currentUser.getId())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't send message to yourself!");
            alert.setHeaderText("Can't send message to yourself!");
            alert.showAndWait();
            return;
        }

        if(destination == null)
            return;
        if(textarea.getText().strip().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't send message request!");
            alert.setHeaderText("Can't send message request!");
            alert.showAndWait();
            return;
        }
        if(selected == null)
            superService.addMessageBetweenTwoUsers(currentUser,destination,textarea.getText(),SIMPLE_MESSAGE);
        else
            superService.addMessageBetweenTwoUsers(currentUser,destination,textarea.getText(),selected.getId());
        this.messagesView.getSelectionModel().clearSelection();
        this.textarea.clear();
        updateMessages();
    }

    public void logout(ActionEvent actionEvent) {

    }

    public void showButtonClick(ActionEvent actionEvent) {
        updateMessages();
    }

    public void findButtonCLick(ActionEvent actionEvent) {
        String first_name = searchTextField.getText();
        if(first_name.strip().isBlank())
            return;
        updateUsersAfterSearch(first_name);
    }

    public void refreshButtonCLick(ActionEvent actionEvent) {
        updateUsers();
    }

    public void backButtonCLick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Pixel");
            current.setScene(scene);
            Main2Controller ctrl = fxmlLoader.getController();
            /*List<User> found = superService.findUsersByName(usernameTextField.getText());
            if (found.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("This user doesn't exist!\n");
                alert.showAndWait();
                return;
            }*/
            ctrl.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonClick(ActionEvent actionEvent) {
        Message selected = messagesView.getSelectionModel().getSelectedItem();
        User destination = usersTableView.getSelectionModel().getSelectedItem();
        if(destination == null)
            return;
        if(selected == null)
            return;
        else {
            if(!selected.getIdFrom().equals(currentUser.getId())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Can't delete messages from another user!\n");
                alert.showAndWait();
                return;
            }
            else{
                superService.deleteMessage(selected);
            }

        }
        this.messagesView.getSelectionModel().clearSelection();
        updateMessages();
    }

    public void undoButtonClick(ActionEvent actionEvent) {
        Message selected = messagesView.getSelectionModel().getSelectedItem();
        User destination = usersTableView.getSelectionModel().getSelectedItem();
        if(destination == null)
            return;
        if(selected == null)
            return;
        else {
            if(!selected.getIdFrom().equals(currentUser.getId())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Can't undo deleted messages from another user!\n");
                alert.showAndWait();
                return;
            }
            else{
                if(selected.getDeleteStatus().equals("deleted")){
                    superService.undoDeleteMessage(selected);
                }
                else{
                    return;
                }
            }

        }
        this.messagesView.getSelectionModel().clearSelection();
        //updateMessages();
    }


    static final class ListViewCell extends ListCell<Message> {
        private final Long idCurrentUser;
        private List<Message> convo = null;

        public ListViewCell(Long userId) {
            this.idCurrentUser = userId;
        }

        public void setConvo(List<Message> convo){
            this.convo = convo;
        }

        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Label label = null;
                VBox vBox = new VBox();
                //Messages from currentUser
                if (item.getIdFrom().equals(idCurrentUser)) {
                    vBox.setAlignment(Pos.CENTER_RIGHT);
                    if(item.getDeleteStatus().equals(ACTIVE_MESSAGE) && item.getIdReply().equals(SIMPLE_MESSAGE)) {
                        label = styleLabel(String.format("%d. %s", item.getIndex_in_convo(), item.getMesaj()));
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    else if(item.getDeleteStatus().equals(ACTIVE_MESSAGE) && !item.getIdReply().equals(SIMPLE_MESSAGE)){
                        label = styleLabel(String.format("%d. --reply to %d-- %s", item.getIndex_in_convo(),item.getReplied_message().getIndex_in_convo(),item.getMesaj()));
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    if(item.getDeleteStatus().equals("deleted")){
                        label = styleLabel("<<deleted>>");
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    var reply=item.getReplied_message();
                    if(reply==null)
                        vBox.getChildren().add(label);
                    else {
                        var index_in_convo = reply.getIndex_in_convo();
                        Label textReply = new Label("You replied to: " + index_in_convo);
                        textReply.setAlignment(Pos.CENTER_RIGHT);
                        Label replyLabel = styleReplyLabel(reply.getMesaj());
                        replyLabel.setAlignment(Pos.CENTER_LEFT);
                        vBox.getChildren().addAll(textReply, replyLabel, label);
                    }
                }
                //Messages from other user
                else{
                    vBox.setAlignment(Pos.CENTER_LEFT);
                    if(item.getDeleteStatus().equals(ACTIVE_MESSAGE) && item.getIdReply().equals(SIMPLE_MESSAGE)) {
                        label = styleLabel(String.format("%d. %s", item.getIndex_in_convo(), item.getMesaj()));
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    else if(item.getDeleteStatus().equals(ACTIVE_MESSAGE) && !item.getIdReply().equals(SIMPLE_MESSAGE)){
                        label = styleLabel(String.format("%d. --reply to %d-- %s", item.getIndex_in_convo(),item.getReplied_message().getIndex_in_convo(),item.getMesaj()));
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    if(item.getDeleteStatus().equals("deleted")){
                        label = styleLabel("<<deleted>>");
                        label.setAlignment(Pos.CENTER_RIGHT);
                    }
                    var reply=item.getReplied_message();
                    if(reply==null)
                        vBox.getChildren().add(label);
                    else {
                        var index_in_convo = reply.getIndex_in_convo();
                        Label textReply = new Label("You replied to: " + index_in_convo);
                        textReply.setAlignment(Pos.CENTER_RIGHT);
                        Label replyLabel = styleReplyLabel(reply.getMesaj());
                        replyLabel.setAlignment(Pos.CENTER_LEFT);
                        vBox.getChildren().addAll(textReply, replyLabel, label);
                    }
                }
                setGraphic(vBox);
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
                    "-fx-text-fill: white;");
            return label;
        }

        private Label styleReplyLabel(String msg){
            var label=new Label(msg);
            label.setMinWidth(50);
            label.setMinHeight(50);
            label.setStyle("-fx-hgap: 5px;" +
                    "    -fx-padding: 5px;" +
                    "" +
                    "    -fx-background-color: #bbadff;" +
                    "    -fx-background-radius: 13px;" +
                    "" +
                    "    -fx-border-radius: 13px;" +
                    "    -fx-border-width: 5px;" +
                    "    -fx-border-color: #bbadff;" +
                    "-fx-text-fill: white;");
            return label;
        }
    }

    public void afterLoad(SuperService superService, User currentUser) {
        this.superService = superService;
        this.currentUser = currentUser;
        //this.superService.addObserver(this);
        this.updateMessages();
        this.updateUsers();
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

    public void updateUsers(){
        this.allUsers.clear();
        Iterable<User> users = this.superService.getAllUsers();
        this.setUsernames(users);
    }

    public void updateUsersAfterSearch(String first_name){
        this.allUsers.clear();
        Iterable<User> users = this.superService.findUsersByName(first_name);
        this.setUsernames(users);
    }

    public void setUsernames(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }

    public void updateMessages() {
        this.messages.clear();
        User current_selected_user = usersTableView.getSelectionModel().getSelectedItem();
        if(current_selected_user == null){
            return;
        }
        List<Message> convo = this.superService.getMessagesBetweenTwoUsers(currentUser,current_selected_user);
        for(int i =0 ;i<convo.size();i++) {
            Message current = convo.get(i);
            current.setIndex_in_convo(i);
            if(!current.getIdReply().equals(SIMPLE_MESSAGE)) {
                for (int j = 0; j < i; j++)
                    if (convo.get(j).getId().equals(current.getIdReply())) {
                        current.setReplied_message(convo.get(j));
                    }
            }
            messages.add(current);
        }
    }

    @Override
    public void updateGroupMessages() {

    }

    @Override
    public void updateGroups() {

    }

}
