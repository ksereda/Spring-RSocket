package com.example.clientservice.model;

public class RequestMovie {

    private String id;

    public RequestMovie() {
    }

    public RequestMovie(String id){
        this.id = id;
    }

    public String getName() {
        return id;
    }

    public void setName(String id) {
        this.id = id;
    }
}