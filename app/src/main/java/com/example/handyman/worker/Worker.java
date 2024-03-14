package com.example.handyman.worker;

public class Worker {
    public int id;

    public String name;
    public String email;
    public String profession;
    public String address;

    public Worker(int id, String name, String email, String profession, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
