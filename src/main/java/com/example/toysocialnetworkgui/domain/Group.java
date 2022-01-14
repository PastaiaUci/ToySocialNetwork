package com.example.toysocialnetworkgui.domain;

import java.util.ArrayList;
import java.util.List;

public class Group extends Entity<Long>{
    private List<Long> list_of_members;
    private String name;
    private String image_url = null;

    public Group(List<Long> list_of_members, String name) {
        this.list_of_members = list_of_members;
        this.name = name;
    }

    public Group(String name){
        this.name = name;
        this.list_of_members = new ArrayList<>();
    }

    public List<Long> getList_of_members() {
        return list_of_members;
    }

    public String getName() {
        return name;
    }

    public void setList_of_members(List<Long> list_of_members) {
        this.list_of_members = list_of_members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }
}
