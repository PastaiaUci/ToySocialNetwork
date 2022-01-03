package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.*;

public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;
    String friendshipStatus;
    Long sender;
    Long fr1;
    Long fr2;


    public Friendship(String friendshipStatus, Long fr1, Long fr2, Long sender) {
        this.setId(new Tuple<Long,Long>(fr1,fr2));
        this.fr1 = fr1;
        this.fr2 = fr2;
        this.date = LocalDateTime.now();
        this.friendshipStatus = friendshipStatus;
        this.sender = sender;
    }

    public Friendship(Long fr1, Long fr2, String date, String friendshipStatus, Long sender) {
        this.setId(new Tuple<Long,Long>(fr1,fr2));
        this.fr1 = fr1;
        this.fr2 = fr2;
        this.date = LocalDateTime.now();
        this.friendshipStatus = friendshipStatus;
        this.sender = sender;
    }
    public Friendship(){
        this.date = LocalDateTime.now();
    }

    public Friendship(LocalDateTime date){
        this.date = date;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
    public Long getSender() {
        return sender;
    }
    public String getFriendshipStatus() {
        return friendshipStatus;
    }
    public Long getFr1(){
        return fr1;
    }
    public Long getFr2(){
        return fr2;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                String.format("(%d,%d)",this.getId().getLeft(),this.getId().getRight())+
                "date=" + date.format(DATE_TIME_FORMATTER) +
                '}';
    }
}
