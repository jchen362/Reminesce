package com.example.reminisce;

public class User {
    public String usr;
    public String pwd;
    public Integer money;
    public String id;
    private static long nextId = 0;
    public User() {//put something for default constructor
    }
    public User(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
        this.money = 0;
        this.id = String.format("c-%04d", ++nextId);
    }
    public String getId() { return this.id; }
}