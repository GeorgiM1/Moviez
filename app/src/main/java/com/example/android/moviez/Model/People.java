package com.example.android.moviez.Model;

import java.util.ArrayList;

/**
 * Created by pc on 2/10/2018.
 */

public class People {
    double popularity;
    int id;
    String profile_path;
    String name;
    ArrayList<KnownFor> known_for;
    boolean adult;

    public double getPopularity() {
        return popularity;
    }

    public int getId() {
        return id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getName() {
        return name;
    }

    public ArrayList<KnownFor> getKnown_for() {
        return known_for;
    }

    public boolean isAdult() {
        return adult;
    }
}
