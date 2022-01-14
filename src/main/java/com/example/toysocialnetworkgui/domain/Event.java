package com.example.toysocialnetworkgui.domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event extends Entity<Long>{
    private String Descriere;
    private String date;
    private String name;


    public Event(String nume, String description,String date) {
        this.Descriere = description;
        this.date = date;
        this.name = nume;
    }


    public String getDescriere() {
        return Descriere;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}