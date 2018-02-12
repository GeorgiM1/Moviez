package com.example.android.moviez.Api;

import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.PeopleModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pc on 2/8/2018.
 */

public interface ApiService {
    @GET("movie/now_playing")
    Call<Model> getNowPlaying ();

    @GET("movie/popular")
    Call<Model> getPopular ();

    @GET("movie/top_rated")
    Call<Model> getTopRated ();

    @GET("movie/upcoming")
    Call<Model> getUpcoming ();

    @GET ("search/movie")
    Call<Model> getSearchedMovie (@Query("query") String term);

    @GET ("person/popular")
    Call<PeopleModel> getPeople ();

    @GET ("search/person")
    Call<PeopleModel> getSearchedPeople (@Query("query") String query);

}
