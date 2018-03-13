package com.example.android.moviez.Model;

import java.util.ArrayList;

/**
 * Created by pc on 2/22/2018.
 */

public class MovieCredits {
    int id;
    ArrayList<Cast> cast = new ArrayList<>();
    ArrayList<Crew> crew = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
    }
}

