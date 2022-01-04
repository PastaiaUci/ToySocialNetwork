package com.example.toysocialnetworkgui.service;

import com.example.toysocialnetworkgui.domain.Friendship;
import com.example.toysocialnetworkgui.domain.Message;
import com.example.toysocialnetworkgui.domain.Tuple;
import com.example.toysocialnetworkgui.domain.User;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.*;
import static com.example.toysocialnetworkgui.Utils.constants.RepoConstants.*;
import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.TEMPORARY_MESSAGE_ID;

public class SuperService {
    private FriendshipService friendshipService = null;
    private UserService userService = null;
    private MessageService messageService = null;


    public SuperService(FriendshipService friendshipService, UserService userService, MessageService messageService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.messageService = messageService;


    }

    public int addUser(String firstName, String lastName, String password) {
        List<User> op = StreamSupport.stream(userService.findAll().spliterator(), false)
                .filter(x -> x.getFirstName().matches(firstName) && x.getLastName().matches(lastName) && x.getPassword().matches(password))
                .collect(Collectors.toList());
        if (!op.isEmpty())
            return UNSUCCESFUL_OPERATION_RETURN_CODE;
        User newUser = new User(firstName, lastName, password);
        userService.addUser(newUser);
        return SUCCESFUL_OPERATION_RETURN_CODE;
    }

    public void removeUser(User user) {
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(x -> x.getId().getLeft().equals(user.getId()) || x.getId().getRight().equals(user.getId()))
                .forEach(x -> {
                    friendshipService.deleteFriendship(new Tuple<Long, Long>(x.getId().getLeft(), x.getId().getRight()));
                });
        userService.removeUser(user.getId());
    }

    public void deleteFriendForUser(User user, User friend) {
        friendshipService.deleteFriendship(new Tuple<>(user.getId(), friend.getId()));
    }


    public List<User> findAllFriendsMathcingNameForGivenUser(User user, String name) {
        List<User> friends_found = new ArrayList<>();
        for (User friend : getAllFriendsForGivenUser(user))
            if (friend.getFirstName().matches(name))
                friends_found.add(friend);
        return friends_found;
    }

    public List<User> findUsersByName(String name) {
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false)
                .filter(x -> x.getFirstName().matches(name))
                .collect(Collectors.toList());
        return users;
    }

    public Set<User> getAllFriendsForGivenUser(User user) {
        Set<User> users1 = StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship -> friendship.getId().getLeft().equals(user.getId()) || friendship.getId().getRight().equals(user.getId()))
                .map(friendship -> {
                    if (friendship.getId().getLeft().equals(user.getId()))
                        return userService.findUserByID(friendship.getId().getRight());
                    else return userService.findUserByID(friendship.getId().getLeft());
                })
                .collect(Collectors.toSet());
        return users1;
    }

    public List<Message> getMessagesBetweenTwoUsers(User user1, User user2) {
        List<Message> convo = StreamSupport.stream(messageService.findAll().spliterator(), false)
                .filter(message -> (message.getIdFrom().equals(user1.getId()) && message.getIdTo().equals(user2.getId())) ||
                        (message.getIdFrom().equals(user2.getId()) && message.getIdTo().equals(user1.getId())) ||
                        (message.getIdFrom().equals(user1.getId()) && message.getIdTo().equals(user1.getId())) && checkIfSelfReplyBelongsToRightConversation(user1, user2, message) ||
                        (message.getIdFrom().equals(user2.getId()) && message.getIdTo().equals(user2.getId())) && checkIfSelfReplyBelongsToRightConversation(user1, user2, message))
                .sorted(Comparator.comparing(Message::getDataTrimitere))
                .collect(Collectors.toList());
        return convo;
    }

    public Set<User> getUsersWithWhomExistsAConversation(User current_user){
        Set<User> users = new HashSet<>();
        for(Message message: messageService.findAll()){
            if(message.getIdFrom().equals(current_user.getId()) && message.getIdTo() != current_user.getId()) {
                users.add(userService.findUserByID(message.getIdTo()));
            }
            if(message.getIdTo().equals(current_user.getId()) && message.getIdFrom() != current_user.getId()){
                users.add(userService.findUserByID(message.getIdFrom()));
            }
            /*if(message.getIdTo().equals(current_user.getId()) && message.getIdFrom().equals(current_user.getId())){
                Message current_message = message;
                while (current_message.getIdFrom().equals(current_message.getIdTo())) {
                    current_message = messageService.findMessageById(current_message.getIdReply());
                }
                if(current_message.getIdFrom() != current_user.getId())
                    users.add(userService.findUserByID(current_message.getIdFrom()));
                if(current_message.getIdTo() != current_user.getId())
                    users.add(userService.findUserByID(current_message.getIdTo()));
            }*/
        }
        return users;
    }

    public void replyAll(User user_from, String message){
        Set<User> users_with_convo = getUsersWithWhomExistsAConversation(user_from);
        for(User user : users_with_convo){
            List<Message> current_conversation = getMessagesBetweenTwoUsers(user_from, user);
            if(!current_conversation.get(current_conversation.size()-1).getIdFrom().equals(user_from.getId()))
                addMessageBetweenTwoUsers(user_from,user,message, current_conversation.get(current_conversation.size()-1).getId());
        }
    }

    public void addMessageBetweenTwoUsers(User user1, User user2, String message, Long reply_status) {
        Message new_message = null;
        if (reply_status.equals(SIMPLE_MESSAGE))
            new_message = new Message(user1.getId(), user2.getId(), message, SIMPLE_MESSAGE);
        else
            new_message = new Message(user1.getId(), user2.getId(), message, reply_status);
        new_message.setId(TEMPORARY_MESSAGE_ID);
        messageService.addMessage(new_message);
    }

    public void deleteMessage(Message message) {
        messageService.deleteMessage(message.getId());
    }

    public void undoDeleteMessage(Message message) {
        message.setDeleteStatus(ACTIVE_MESSAGE);
        messageService.undoMessageDelete(message);
    }

    public User findUserById(Long id) {
        return userService.findUserByID(id);
    }

    private boolean checkIfSelfReplyBelongsToRightConversation(User user1, User user2, Message message) {
        Message current_message = message;
        while (current_message.getIdFrom().equals(current_message.getIdTo())) {
            current_message = messageService.findMessageById(current_message.getIdReply());
        }
        if (current_message.getIdFrom().equals(user1.getId()) && current_message.getIdTo().equals(user2.getId()) ||
                current_message.getIdFrom().equals(user2.getId()) && current_message.getIdTo().equals(user1.getId()))
            return true;
        return false;
    }

    public Iterable<Friendship> getAllFriendships(Long id) {

        Iterable<Friendship> allFriendships = friendshipService.repo.findAll();
        Set<Friendship> friendships = new HashSet<>();
        for (Friendship friendship : allFriendships)
            if (friendship.getFriendshipStatus().equals("approved") && (friendship.getFr1() == id || friendship.getFr2() == id))
                friendships.add(friendship);

        return friendships;

    }

    public User findOneUser(Long messageTask) {
        User user = userService.repo.findOne(messageTask);
        return user;
    }


    public void responseToFriendRequest(Long idFrom, Long idTo, String response) throws ServiceException {
        Long sender = idFrom;
        if (idFrom > idTo) {
            Long swap = idFrom;
            idFrom = idTo;
            idTo = swap;
        }
        if (friendshipService.repo.findOne(new Tuple<Long, Long>(idFrom, idTo)) == null)
            throw new ServiceException("Invalid friend request!");
        Friendship newFriendship = new Friendship(response, idFrom, idTo, sender);
        friendshipService.repo.update(newFriendship);
    }


    public List<Friendship> pendingFriendships(Long id) {
        List<Friendship> pendingFriendships = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipService.repo.findAll();
        for (Friendship friendship : friendships) {
            if (((friendship.getId().getLeft().equals(id) && !friendship.getId().getLeft().equals(friendship.getSender()))
                    || (friendship.getId().getRight().equals(id) && !friendship.getId().getRight().equals(friendship.getSender())))
                    && friendship.getFriendshipStatus().equals("pending"))
                pendingFriendships.add(friendship);
        }
        return pendingFriendships;
    }

    public void sendFriendRequest(Long idFrom, Long idTo) throws ServiceException {
        Long sender = idFrom;
        if (idFrom > idTo) {
            Long swap = idFrom;
            idFrom = idTo;
            idTo = swap;
        }
        if (userService.findUserByID(idTo) == null)
            throw new ServiceException("invalid id!\n");
        if (friendshipService.repo.findOne(new Tuple<Long, Long>(idFrom, idTo)) != null)
            throw new ServiceException("There is already a friendship/friend request/rejected friend request between these users\n");
        if (idTo == idFrom)
            throw new ServiceException("Wtf cant send a friend request yourself\n");
        Friendship friendship = new Friendship("pending", idFrom, idTo, sender);
        friendshipService.repo.save(friendship);
    }
    public Iterable<User> getAllUsers(){
        return userService.findAll();
    }


    //login function
    public boolean login(String username, String password) {
        if(userService.getUsersWithUsername(username)!=null){
            List<User> users = userService.getUsersWithUsername(username);
            for(User user:users){

                if (user.getPassword().equals(password))
                    return true;
            }
        }
        return false;
    }

    public List<User> incomingFriendRequests(Long id){
        List<User> usersWhoSentFriendRequest = new ArrayList<>();
        Iterable<Friendship> pendingFriendships = friendshipService.findAll();
        for(Friendship friendship:pendingFriendships){
            if( friendship.getFr1()!=id ){
                if(friendship.getFr1() == friendship.getSender()){
                    User user = userService.findUserByID(friendship.getFr1());
                    usersWhoSentFriendRequest.add(user);
                }
                else{
                    if(friendship.getFr2() == friendship.getSender()){
                        User user = userService.findUserByID(friendship.getFr2());
                        usersWhoSentFriendRequest.add(user);
                    }
                }
            }
        }
        return usersWhoSentFriendRequest;
    }


    public List<User> sentFriendRequests(Long id){

        List<User> sentFriendRequest = new ArrayList<>();
        Iterable<Friendship> pendingFriendships = friendshipService.findAll();
        for(Friendship friendship:pendingFriendships){


            if( friendship.getFr1()!=id ) {

                if (friendship.getFr1() != friendship.getSender()) {
                    User user = userService.findUserByID(friendship.getFr1());
                    sentFriendRequest.add(user);
                }
            }
            else{

                    if(friendship.getFr2() != friendship.getSender()){
                        User user = userService.findUserByID(friendship.getFr2());
                        sentFriendRequest.add(user);
                    }
                }

        }
        return sentFriendRequest;
    }

}

