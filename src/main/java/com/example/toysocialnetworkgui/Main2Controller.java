package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Event;
import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.ServiceException;
import com.example.toysocialnetworkgui.service.SuperService;
import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main2Controller {


    private static SuperService superService;
    private static User currentUser;

    ObservableList<Friendship> allRequests = FXCollections.observableArrayList();
    static ObservableList<Event> allEvents = FXCollections.observableArrayList();





    @FXML
    Button logoutButton;
    @FXML
    Button viewFriendsButton;
    @FXML
    Button messagesButton;

    @FXML
    Button eventsButton;

    @FXML
    Button unsubButton;


    @FXML
    Label nameLabel;

    @FXML
    TableView<Friendship> friendshipTableView;

    @FXML
    TableView<Event> eventTableView;
    @FXML
    TableColumn<Friendship,String> numeColumn;
    @FXML
    TableColumn<Friendship,String> descColumn;
    @FXML
    TableColumn<Friendship,String> dataColumn;

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

        numeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("Descriere"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventTableView.setItems(allEvents);

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
        nameLabel.setText(currentUser.getLastName());
        this.updateRequests();
        this.updateAllEvents();
    }

    public void updateAllEvents(){
        this.allEvents.clear();
        Iterable<Event> events = this.superService.getAllEventsForUser(currentUser.getId());
        if(events == null){
            System.out.println("lskfsdmn");
        }
        this.setEvents(events);
    }
    public void setEvents(Iterable<Event> events) {
        events.forEach( u -> this.allEvents.add(u));
    }


    public void updateRequests(){
        this.allRequests.clear();
        Iterable<Friendship> friendships = this.superService.allRequestsOfAUser(currentUser.getId());
        this.setFriendships(friendships);
    }

    public void setFriendships(Iterable<Friendship> friendships) {
        friendships.forEach( u -> this.allRequests.add(u));
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
    public void unsubRequest() {
        superService.unsubscribeUserToEvent(currentUser.getId(),eventTableView.getSelectionModel().getSelectedItem().getId());
        this.updateAllEvents();

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




    @FXML
    public void cancelRequest(){

        if (friendshipTableView.getSelectionModel().getSelectedItem() == null)
            return;

        Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (currentUser.getId() == friendship.getSender()  && (currentUser.getId() == friendship.getFr1() || currentUser.getId() == friendship.getFr2())  )
        {
            //delete friendship from db
            if(friendship.getFriendshipStatus().equals("pending")) {
                this.superService.deleteFriendship(friendship.getFr1(), friendship.getFr2());
                this.updateRequests();
            }
        }
    }

    @FXML
    public void logout(ActionEvent event){
        try {
            Node source = (Node) event.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Ian");
            current.setScene(scene);
            LoginController mainController = fxmlLoader.getController();
            mainController.setServiceController(superService);

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onFriendsButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("friends-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Ian");
            current.setScene(scene);
            FriendsListController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessagesButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("messages-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Messages");
            current.setScene(scene);
            ChatController ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editButtonClick(ActionEvent actionEvent) {
        friendshipTableView.setVisible(false);
    }

    public void groupsButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("groups-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Ian");
            current.setScene(scene);
            GroupsController mainController = fxmlLoader.getController();
            mainController.setServiceController(superService);
            //mainController.afterLoad(superService,superService.findUsersByName(currentUser.getFirstName()).get(0));
            mainController.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void eventsButtonClick(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("events-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            current.setTitle("Ian");
            current.setScene(scene);
            EventController mainController = fxmlLoader.getController();
            mainController.setServiceController(superService);
            //mainController.afterLoad(superService,superService.findUsersByName(currentUser.getFirstName()).get(0));
            mainController.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static  void startThread(){
        Thread thread = new Thread(() -> {

                try {
                    Thread.sleep(100);
                    for (Event event : allEvents) {
                            long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), event.getDate());
                            if ( (minutes > 0 && minutes <1440) && superService.isNotificationOn(currentUser.getId(),event.getId()) ) {
                                Platform.runLater(() -> {
                                    String text = event.getName().toUpperCase(Locale.ROOT) + " will start in less than an day!!!";

                                    Stage dialog = new Stage();
                                    dialog.setResizable(false);
                                    VBox dialogVbox = new VBox(20);
                                    dialogVbox.setStyle("-fx-background-color: #F2003C");

                                    Label label = new Label(text);
                                    label.setStyle("-fx-font-weight: bold;" +
                                            "-fx-font-family: 'Times New Roman';" +
                                            "-fx-text-fill: white;" +
                                            "-fx-font-size: 20px;" +
                                            "-fx-padding: 10 10 10 10;"
                                    );

                                    dialogVbox.getChildren().add(label);
                                    Scene dialogScene = new Scene(dialogVbox, 600, 50);

                                    dialog.setScene(dialogScene);
                                    dialog.show();
                                });
                            }

                    }

                } catch (Error | InterruptedException e) {
                    System.out.println(e.getMessage());

                }


        });
        thread.start();
    }

    public void offNotifications() {
        superService.turnOffNotifications(currentUser.getId(),eventTableView.getSelectionModel().getSelectedItem().getId());
    }

    public void onNotifications(ActionEvent actionEvent) {
        superService.turnOnNotifications(currentUser.getId(),eventTableView.getSelectionModel().getSelectedItem().getId());
    }
}

