package com.teamrbm.alarmapp.data.model;

import java.util.Date;

public class Alarm {
     
    //private variables
    int id;
    String name;
    Date date;

    public Alarm() {
    }

    public Alarm(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}