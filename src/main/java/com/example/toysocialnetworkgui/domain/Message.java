package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.*;

public class Message extends Entity<Long>{
    private Long id_from;
    private Long id_to;
    private String mesaj;
    private LocalDateTime data_trimitere;
    private Long id_reply;
    private String delete_status;

    /**
     * Constructor for a message from the data-base
     * @param id_from
     *              id of the sender
     * @param id_to
     *              id of the receiver
     * @param message
     *              the actual message sent
     * @param date
     *              date of sending
     * @param id_reply
     *              the id of the message to which the new message replies
     */
    public Message(Long id_from, Long id_to, String message, LocalDateTime date, Long id_reply, String delete_status){
        this.id_from = id_from;
        this.id_to = id_to;
        this.mesaj = message;
        this.data_trimitere = date;
        this.id_reply = id_reply;
        this.delete_status =  delete_status;
    }

    /**
     * Constructor for a message that is a reply to another message sent from a user to another
     * or from the user to himself
     * @param id_from
     *              id of the sender
     * @param id_to
     *              id of the receiver
     * @param message
     *              the actual message sent
     * @param id_reply
     *              the id of the message to which the new message replies
     */
    public Message(Long id_from, Long id_to, String message, Long id_reply){
        this.id_from = id_from;
        this.id_to = id_to;
        this.mesaj = message;
        this.data_trimitere = LocalDateTime.now();
        this.id_reply = id_reply;
        this.delete_status =  ACTIVE_MESSAGE;
    }

    /**
     * Constructor for a simple message that is not a reply to another message sent from a user to another
     * @param id_from
     *              id of the sender
     * @param id_to
     *              id of the receiver
     * @param message
     *              the actual message sent
     */
    public Message(Long id_from, Long id_to, String message){
        this.id_from = id_from;
        this.id_to = id_to;
        this.mesaj = message;
        this.data_trimitere = LocalDateTime.now();
        this.id_reply = SIMPLE_MESSAGE;
        this.delete_status =  ACTIVE_MESSAGE;
    }

    public Long getIdFrom() {
        return id_from;
    }

    public Long getIdTo() {
        return id_to;
    }

    public String getMesaj() {
        return mesaj;
    }

    public LocalDateTime getDataTrimitere() {
        return data_trimitere;
    }

    public Long getIdReply() {
        return id_reply;
    }

    public String getDeleteStatus() {
        return delete_status;
    }

    public void setDeleteStatus(String new_status){
        this.delete_status = new_status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        Message that = (Message) obj;
        return getIdFrom().equals(that.getIdFrom()) &&
                getIdTo().equals(that.getIdTo()) &&
                getMesaj().equals(that.getMesaj()) &&
                getDataTrimitere().equals(that.getDataTrimitere()) &&
                getIdReply().equals(that.getIdReply()) &&
                getDeleteStatus().equals(that.getDeleteStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_from,id_to,mesaj,data_trimitere,id_reply,delete_status);
    }

}
