package com.example.blitz_github.Utils.Project;

import java.sql.Timestamp;

public class projectModel {

    private String title;
    private String client;
    private String role;
    private String description;
    private String start;
    private String end;
    public projectModel(){}
    public projectModel(String title, String client, String role, String description, String start, String end) {
        this.title = title;
        this.description = description;
        this.role = role;
        this.client = client;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }



}
