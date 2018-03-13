package com.example.android.moviez.Model;

import java.util.Date;

/**
 * Created by pc on 2/22/2018.
 */

public class PeopleDetails {

    String birthday;
    Date deathday;
    int id;
    String name;
    int gender;
    String biography;
    double popularity;
    String place_of_birth;
    String profile_path;
    boolean adult;
    String imdb_id;

    public String getBirthday() {
        return birthday;
    }

    public Date getDeathday() {
        return deathday;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getBiography() {
        return biography;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getImdb_id() {
        return imdb_id;
    }
}
