package com.example.android.moviez.other;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.Movie;
import com.google.gson.Gson;

/**
 * Created by pc on 2/12/2018.
 */

public class PreferencesManager {

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences("MySharedPreferencesFile", Activity.MODE_PRIVATE);
    }

    public static void addMovie(Movie people, Context c) {
        Gson gson = new Gson();
        String mapStrnig = gson.toJson(people);
        getPreferences(c).edit().putString("people", mapStrnig).apply();
    }

    public static Movie getMovie(Context context) {
        return new Gson().fromJson(getPreferences(context).getString("people", ""), Movie.class);
    }

    public static void addSessionId(String s, Context c) {
        getPreferences(c).edit().putString("session_id", s).apply();
    }

    public static String getSessionId(Context c) {
        return getPreferences(c).getString("session_id", "");
    }

    public static void addFavMovie(Model movie, Context c) {
        Gson gson = new Gson();
        String mapStrnig = gson.toJson(movie);
        getPreferences(c).edit().putString("favMovie", mapStrnig).apply();
    }

    public static Model getFavMovie(Context context) {
        return new Gson().fromJson(getPreferences(context).getString("favMovie", ""), Model.class);
    }

  public  static void addWatchlistMovies (Model movie, Context context){
      Gson gson = new Gson();
      String mapStrnig = gson.toJson(movie);
      getPreferences(context).edit().putString("watchlistMovie", mapStrnig).apply();

  }
  public static Model getWatchlistMovies (Context context){
      return new Gson().fromJson(getPreferences(context).getString("watchlistMovie", ""), Model.class);
  }
}


