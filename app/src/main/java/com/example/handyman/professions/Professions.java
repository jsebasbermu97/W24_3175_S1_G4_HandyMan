package com.example.handyman.professions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Professions {
    Carpenter("Carpenter"),
    Electrician("Electrician"),
    Plumber("Plumber"),
    Painter("Painter");

    private static List<String> professions;
    private String profession;

    static {
        professions = new ArrayList<>();
        for (Professions profession : Professions.values()) {
            professions.add(profession.profession);
        }
    }

    private Professions(String profession) {
        this.profession = profession;
    }

    public static List<String> getProfessions() {
        return Collections.unmodifiableList(professions);
    }

    @Override
    public String toString(){
        return profession;
    }


}
