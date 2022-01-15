package com.example.toysocialnetworkgui.domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event extends Entity<Long>{
    private String Descriere;
    private LocalDateTime date;
    private String name;


    public Event(String nume, String description,LocalDateTime date) {
        this.name = nume;
        this.Descriere = description;
        this.date = date;
    }


    public String getDescriere() {
        return Descriere;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}