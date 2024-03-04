package com.example.handyman.worker;

public enum Professions {
    Carpenter("Carpenter"),
    Electrician("Electrician"),
    Plumber("Plumber"),
    Painter("Painter");

    private String profession;

    Professions(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString(){
        return "\n" + profession + "\n";
    }
}
