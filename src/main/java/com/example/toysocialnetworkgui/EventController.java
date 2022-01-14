package com.example.toysocialnetworkgui;

import com.example.toysocialnetworkgui.domain.Event;
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


public class EventController {


    SuperService superService;
    private User currentUser;
    @FXML
    TableView<Event> eventsTableView;
    @FXML
    TableColumn<Event,String> nameColumn;
    @FXML
    TableColumn<Event,String> descriereColumn;
    @FXML
    TableColumn<Event,String> dataColumn;
    @FXML
    TextField nameTextField;
    @FXML
    TextField descriereTextField;
    @FXML
    TextField dataTextField;
    @FXML
    Button backButton;
    @FXML
    Button addButton;
    @FXML
    Button subButton;



    ObservableList<Event> allEvents = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriereColumn.setCellValueFactory(new PropertyValueFactory<>("Descriere"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        eventsTableView.setItems(allEvents);
    }

    public void setServiceController(SuperService superService) {
        this.superService = superService;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void updateAllEvents(){
        this.allEvents.clear();
        Iterable<Event> events = this.superService.getAllEvents();
        this.setEvents(events);
    }

    public void setEvents(Iterable<Event> events) {
        events.forEach( u -> this.allEvents.add(u));
    }


    public void afterLoad(SuperService superService, User user) {
        this.setServiceController(superService);
        this.setCurrentUser(user);
        this.updateAllEvents();
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 896.0, 578);
            current.setTitle("Ian");
            current.setScene(scene);
            Main2Controller mainController = fxmlLoader.getController();
            mainController.afterLoad(superService,currentUser);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddButtonClick() {

        String nume = nameTextField.getText();
        String descriere = descriereTextField.getText();
        String data = dataTextField.getText();
        if(nume.equals("") || descriere.equals("") || data.equals("") ) {
            return;
        }
        superService.addEvent(nume,descriere,data);
        this.updateAllEvents();
    }

    @FXML
    public void subButtonClick() {
            if (eventsTableView.getSelectionModel().getSelectedItem() == null)
                return;
            superService.subscribeUserToEvent(currentUser.getId(), eventsTableView.getSelectionModel().getSelectedItem().getId());

            this.updateAllEvents();
    }

}
