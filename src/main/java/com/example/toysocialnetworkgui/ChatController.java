package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ListView.*;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.ACTIVE_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.SIMPLE_MESSAGE;

public class ChatController {

    protected SuperService superService;
    private User currentUser, destination;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
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
    public void initialize() {
        this.messagesView.setCellFactory(param -> new ListViewCell(currentUser.getId()));
        this.messagesView.setItems(messages);
    }


    public void resetReply(ActionEvent actionEvent) {

    }

    public void sendMessageButtonClick(ActionEvent actionEvent) {
        Message selected = messagesView.getSelectionModel().getSelectedItem();
        if(textarea.getText().strip().isBlank()) {
            System.out.println("stefaneeee");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't send friend request!");
            alert.setHeaderText("Mi ai dat leanul pe jos!");
            alert.showAndWait();
        }
        if(selected == null)
            superService.addMessageBetweenTwoUsers(currentUser,destination,textarea.getText(),SIMPLE_MESSAGE);
        else
            superService.addMessageBetweenTwoUsers(currentUser,destination,textarea.getText(),selected.getId());
        this.messagesView.getSelectionModel().clearSelection();
        this.textarea.clear();
        updateMessages();
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

    public void afterLoad(SuperService superService, User currentUser, User destination) {
        this.superService = superService;
        this.currentUser = currentUser;
        this.destination = destination;
        //this.superService.addObserver(this);

        this.destinationLabel.setText(destination.getFirstName() + " " + destination.getLastName());
        this.updateMessages();
    }

    public void updateMessages() {
        this.messages.clear();
        List<Message> convo = this.superService.getMessagesBetweenTwoUsers(currentUser,destination);
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

}
