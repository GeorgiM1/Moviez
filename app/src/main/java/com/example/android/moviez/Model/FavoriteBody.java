package com.example.android.moviez.Model;

/**
 * Created by pc on 3/1/2018.
 */

public class FavoriteBody {
    String media_type;
    int media_id;
    boolean favorite;

    public String getMedia_type() {
        return media_type;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;


    }
}
