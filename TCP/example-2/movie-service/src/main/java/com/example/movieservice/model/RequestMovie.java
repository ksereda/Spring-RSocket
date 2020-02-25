package com.example.movieservice.model;

public class RequestMovie {

    private String name;

    public RequestMovie() {
    }

    public RequestMovie(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}