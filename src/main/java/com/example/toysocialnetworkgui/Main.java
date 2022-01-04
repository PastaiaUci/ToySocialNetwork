package com.example.toysocialnetworkgui;
import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.User;
import com.example.toysocialnetworkgui.domain.validators.FriendshipValidator;
import com.example.toysocialnetworkgui.domain.validators.MessageValidator;
import com.example.toysocialnetworkgui.domain.validators.UserValidator;
import com.example.toysocialnetworkgui.repository.Repository;
import com.example.toysocialnetworkgui.repository.database.FriendshipsDbRepository;
import com.example.toysocialnetworkgui.repository.database.MessageDbRepository;
import com.example.toysocialnetworkgui.repository.database.UserDbRepository;
import com.example.toysocialnetworkgui.repository.repoExceptions.FileError;
import com.example.toysocialnetworkgui.repository.repoExceptions.RepoException;
import com.example.toysocialnetworkgui.service.FriendshipService;
import com.example.toysocialnetworkgui.service.MessageService;
import com.example.toysocialnetworkgui.service.SuperService;
import com.example.toysocialnetworkgui.service.UserService;
import com.example.toysocialnetworkgui.ui.Runner;



public class Main {
    public static void main(String[] args) {

        String fileName="data/users.csv";
        String fileName2="data/friendships.csv";
        Repository<Long, User> userFileRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = null;
        Repository<Long, User> userDbRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository = null;
        Repository<Long, Message> messageDbRepository = null;
        try {
            userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres", new UserValidator());
            friendshipDbRepository = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres",new FriendshipValidator());
            messageDbRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/postgres","postgres","postgres", new MessageValidator());
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
        SuperService superService = new SuperService(friendshipService,userService,messageService);
        Runner runner = new Runner(superService);
        runner.runApp();

    }
}



