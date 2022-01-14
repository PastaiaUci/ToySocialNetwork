package com.example.toysocialnetworkgui.domain;

import java.time.LocalDateTime;

import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.ACTIVE_MESSAGE;
import static com.example.toysocialnetworkgui.Utils.constants.DomainConstants.SIMPLE_MESSAGE;

public class GroupMessage extends Entity<Long>{
    private Long id__user_from;
    private Long id_group_to;
    private String mesaj;
    private LocalDateTime data_trimitere;
    private Long id_reply;
    private String delete_status;
    private int index_in_convo = -1;
    private Message replied_message = null;

    public GroupMessage(Long id__user_from, Long id_group_to, String mesaj, LocalDateTime data_trimitere, Long id_reply, String delete_status) {
        this.id__user_from = id__user_from;
        this.id_group_to = id_group_to;
        this.mesaj = mesaj;
        this.data_trimitere = data_trimitere;
        this.id_reply = id_reply;
        this.delete_status = delete_status;
    }

    public GroupMessage(Long id_from, Long id_to, String message){
        this.id__user_from = id_from;
        this.id_group_to = id_to;
        this.mesaj = message;
        this.data_trimitere = LocalDateTime.now();
        this.id_reply = SIMPLE_MESSAGE;
        this.delete_status =  ACTIVE_MESSAGE;
    }

    public Long getId__user_from() {
        return id__user_from;
    }

    public Long getId_group_to() {
        return id_group_to;
    }

    public String getMesaj() {
        return mesaj;
    }

    public LocalDateTime getData_trimitere() {
        return data_trimitere;
    }

    public Long getId_reply() {
        return id_reply;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public int getIndex_in_convo() {
        return index_in_convo;
    }

    public Message getReplied_message() {
        return replied_message;
    }

}
