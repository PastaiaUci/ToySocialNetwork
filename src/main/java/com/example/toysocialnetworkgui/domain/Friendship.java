package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;

import static com.example.toysocialnetworkgui.Utils.constants.ValidatorConstants.*;

//public class Prietenie extends Entity<Tuple<Long,Long>>
public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;
    //String date;

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

    @Override
    public String toString() {
        return "Prietenie{" +
                String.format("(%d,%d)",this.getId().getLeft(),this.getId().getRight())+
                "date=" + date.format(DATE_TIME_FORMATTER) +
                '}';
    }
}
