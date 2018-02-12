package com.example.android.moviez.Model;

import java.util.ArrayList;

/**
 * Created by pc on 2/10/2018.
 */

public class KnownFor {
    double vote_average;
    int vote_count;
    int id;
    boolean video;
    String media_type;
    public String title;
    double popularity;
    String poster_path;
    String original_language;
    String original_title;
    ArrayList<Integer> genre_ids;
    String backdrop_path;
    boolean adult;
    String overview;
    String release_date;

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public int getId() {
        return id;
    }

    public boolean isVideo() {
        return video;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }
}
