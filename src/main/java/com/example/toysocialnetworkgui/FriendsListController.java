package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.Observer.Observable;
import com.example.toysocialnetworkgui.Observer.Observer;
import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.service.ServiceException;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;


public class FriendsListController implements Observer {
    protected SuperService superService;
    private User currentUser;
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private final ObservableList<User> allFriends = FXCollections.observableArrayList();


    @FXML
    ListView<User> allUsersListView;
    @FXML
    ListView<User> allFriendsListView;


    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;



    @FXML
    public void initialize() {

        this.allUsersListView.setCellFactory(param -> new ListViewCell(currentUser));
        this.allFriendsListView.setCellFactory(param->new ListViewCell(currentUser));
        this.allUsersListView.setItems(allUsers);
        this.allFriendsListView.setItems(allFriends);
    }



    public void findButtonCLick(ActionEvent actionEvent) {
    }

    public void refreshButtonCLick(ActionEvent actionEvent) {
    }

    @FXML
    public void raport2ButtonClick() throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Main.mainStage);
        if(selectedDirectory == null)
            return;

        int textX = 100;
        AtomicInteger textY = new AtomicInteger(700);
        LocalTime time = LocalTime.of(0,0,0,0);
        LocalDateTime startDate = LocalDateTime.of(startDatePicker.getValue(), time);
        LocalDateTime  endDate = LocalDateTime.of(endDatePicker.getValue(), time);

        User user = allFriendsListView.getSelectionModel().getSelectedItem();
        Iterable<Message> messages_from_user= superService.findAllReceivedMassagesFromUser(currentUser.getId(),user.getId(),startDate,endDate);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage( page );

        PDFont font1 = PDType1Font.HELVETICA_BOLD;
        PDFont font2 = PDType1Font.HELVETICA;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(font1, 20 );
        contentStream.newLineAtOffset(textX,textY.get());
        contentStream.showText("Messages received by " + currentUser.getFirstName() + " from " + user.getLastName()+ ":");
        contentStream.endText();
        textY.addAndGet(-20);



        messages_from_user.forEach(x -> {
            try {
                contentStream.beginText();
                contentStream.setFont( font2, 10 );
                contentStream.newLineAtOffset(textX-20,textY.get());
                contentStream.showText(currentUser.getFirstName() + " a primit mesajul " +x.getMesaj() + " pe data de " +x.getDataTrimitere() +" de la "+user.getFirstName());
                contentStream.endText();
                textY.addAndGet(-10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        contentStream.close();
        document.save( selectedDirectory+"/"+currentUser.getFirstName()+" "+currentUser.getLastName()+"2.pdf");
        document.close();


    }
    @FXML
    public void pdfButtonClick() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Main.mainStage);

        if(selectedDirectory == null)
            return;

        int textX = 100;
        AtomicInteger textY = new AtomicInteger(700);
        LocalTime time = LocalTime.of(0,0,0,0);
        LocalDateTime startDate = LocalDateTime.of(startDatePicker.getValue(), time);
        LocalDateTime  endDate = LocalDateTime.of(endDatePicker.getValue(), time);

        Iterable<Message> messages_sent = superService.findAllSentMassagesToUsers(currentUser.getId(),startDate,endDate);
        Iterable<Message> messages_received = superService.findAllReceivedMassagesFromUsers(currentUser.getId(),startDate,endDate);
        Iterable<Friendship> friends = superService.getFriendsInInterval(currentUser.getId(),startDate,endDate);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage( page );

        PDFont font1 = PDType1Font.HELVETICA_BOLD;
        PDFont font2 = PDType1Font.HELVETICA;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(font1, 20 );
        contentStream.newLineAtOffset(textX,textY.get());
        contentStream.showText("New friends of " + currentUser.getFirstName() + ":");
        contentStream.endText();
        textY.addAndGet(-20);

        friends.forEach(x -> {
            try {
                contentStream.beginText();
                contentStream.setFont( font2, 10 );
                contentStream.newLineAtOffset(textX-20,textY.get());
                String friend = superService.findUserById(x.getFr1()).getFirstName();
                String user = superService.findUserById(x.getFr2()).getFirstName();
                contentStream.showText(friend + " s-a imprietenit cu  " +  user+ " pe data de " + x.getDate());
                contentStream.endText();
                textY.addAndGet(-10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        textY.addAndGet(-20);
        contentStream.beginText();
        contentStream.setFont(font1, 20 );
        contentStream.newLineAtOffset(textX,textY.get());
        contentStream.showText("Messages sent by " + currentUser.getFirstName() + ":");
        contentStream.endText();
        textY.addAndGet(-20);

        messages_sent.forEach(x -> {
            try {
                contentStream.beginText();
                contentStream.setFont( font2, 10 );
                contentStream.newLineAtOffset(textX-20,textY.get());
                contentStream.showText(currentUser.getFirstName() + " a trimis mesajul " + x.getMesaj() + " pe data de " +x.getDataTrimitere() );
                contentStream.endText();
                textY.addAndGet(-10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        textY.addAndGet(-20);
        contentStream.beginText();
        contentStream.setFont(font1, 20 );
        contentStream.newLineAtOffset(textX,textY.get());
        contentStream.showText("Messages received by " + currentUser.getFirstName() + ":");
        contentStream.endText();
        textY.addAndGet(-20);

        messages_received.forEach(x -> {
            try {
                contentStream.beginText();
                contentStream.setFont( font2, 10 );
                contentStream.newLineAtOffset(textX-20,textY.get());
                contentStream.showText(currentUser.getFirstName() + " a primit mesajul " +x.getMesaj() + " pe data de " +x.getDataTrimitere() );
                contentStream.endText();
                textY.addAndGet(-10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        contentStream.close();
        document.save( selectedDirectory+"/"+currentUser.getFirstName()+" "+currentUser.getLastName()+".pdf");
        document.close();

    }

    public void backButtonClick(ActionEvent actionEvent) {

        try {
            Node source = (Node) actionEvent.getSource();
            Stage current = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 896, 578);
            current.setTitle("Pixel");
            current.setScene(scene);
            Main2Controller ctrl = fxmlLoader.getController();
            ctrl.afterLoad(superService, currentUser);
        }
         catch (IOException e) {

            e.printStackTrace();
        }
    }





    public void deleteButtonClick() {
        if(allFriendsListView.getSelectionModel().getSelectedItem() == null){
            return;
        }
            superService.deleteFriendship(currentUser.getId(),allFriendsListView.getSelectionModel().getSelectedItem().getId());
            //this.updateFriends();
    }

    public void sendRequest() {
        try {
            if (allUsersListView.getSelectionModel().getSelectedItem() == null)
                return;
            this.superService.sendFriendRequest(currentUser.getId(),allUsersListView.getSelectionModel().getSelectedItem().getId());
            //this.updateFriends();
        }
        catch (ServiceException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't send friend request!");
            alert.setHeaderText("Can't really send a friend request!");
            alert.showAndWait();

        }

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

    @Override
    public void updateGroups() {

    }

    public void afterLoad(SuperService superService, User currentUser) {
        this.superService = superService;
        this.currentUser = currentUser;
        this.updateUsers();
        this.updateFriends();
        this.superService.addObserver(this);
    }

    @Override
    public void updateUsers() {
        this.allUsers.clear();
        Iterable<User> allUserss = this.superService.getAllUsers();
        this.setUsers(allUserss);
    }

    @Override
    public void updateMessages() {

    }

    @Override
    public void updateGroupMessages() {

    }

    @Override
    public void updateFriends(){
        this.allFriends.clear();
        Iterable<User> allFriends = this.superService.getAllFriendsForGivenUser(currentUser);
        this.setFriends(allFriends);
    }

    @Override
    public void updateRequests() {

    }

    @Override
    public void updateEvents() {

    }

    public void setFriends(Iterable<User> friendss){
        friendss.forEach(u->this.allFriends.add(u));
    }

    public void setUsers(Iterable<User> users) {
        users.forEach( u -> this.allUsers.add(u));
    }
}

