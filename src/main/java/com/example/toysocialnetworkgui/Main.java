package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.domain.*;
import com.example.toysocialnetworkgui.domain.validators.EventValidator;
import com.example.toysocialnetworkgui.domain.validators.FriendshipValidator;
import com.example.toysocialnetworkgui.domain.validators.MessageValidator;
import com.example.toysocialnetworkgui.domain.validators.UserValidator;
import com.example.toysocialnetworkgui.repository.Repository;
import com.example.toysocialnetworkgui.repository.database.*;
import com.example.toysocialnetworkgui.repository.repoExceptions.FileError;
import com.example.toysocialnetworkgui.repository.repoExceptions.RepoException;
import com.example.toysocialnetworkgui.service.*;
import com.example.toysocialnetworkgui.ui.Runner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.toysocialnetworkgui.domain.GFG.encryptThisString;


public class Main extends Application {

    public static Window mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //String fileName3= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName="data/users.csv";
        String fileName2="data/friendships.csv";
        Repository<Long, User> userFileRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = null;
        Repository<Long, User> userDbRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository = null;
        Repository<Long, Event> eventDbRepository = null;
        Repository<Long, Message> messageDbRepository = null;
        GroupDbRepository groupDbRepository = null;
        try {
            userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres", new UserValidator());
            friendshipDbRepository = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres",new FriendshipValidator());
            messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres", new MessageValidator());
            eventDbRepository = new EventsDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres", new EventValidator());
            groupDbRepository = new GroupDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres");
        }
        catch (FileError ex){
            System.out.println(ex.getMessage());
            return;
        }
        catch (RepoException ex){
            System.out.println(ex.getMessage());
            return;
        }


        //DataBase
        UserService userService = new UserService(userDbRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDbRepository);
        MessageService messageService = new MessageService(messageDbRepository);
        EventService eventService = new EventService(eventDbRepository);
        GroupsService groupsService = new GroupsService(groupDbRepository);
        SuperService superService = new SuperService(friendshipService,userService,messageService,eventService,groupsService);


       /* User user = superService.findUserById(6L);
        superService.replyAll(user,"salut");*/
       /* System.out.println(superService.findGroupsOfAUser(11L).size());
       Runner runner = new Runner(superService);
       runner.runApp();*/


        /*String str = "GeeksForGeeks";
        System.out.println(encryptThisString(str));*/

       /* Runner runner = new Runner(superService);
        runner.runApp();*/


      //  Runner runner = new Runner(superService);
       // runner.runApp();


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController mainController = fxmlLoader.getController();
        mainController.setServiceController(superService);
        superService.getAllEvents().forEach(
               x->{System.out.println(x.getId());}
        );


        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("IanAztecaAmuly");

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}



