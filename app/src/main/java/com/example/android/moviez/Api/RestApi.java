package com.example.android.moviez.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.moviez.BuildConfig;
import com.example.android.moviez.Model.AuthToken;
import com.example.android.moviez.Model.FavoriteBody;
import com.example.android.moviez.Model.GuestUser;
import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.MovieCredits;
import com.example.android.moviez.Model.PeopleDetails;
import com.example.android.moviez.Model.PeopleModel;
import com.example.android.moviez.Model.RateValue;
import com.example.android.moviez.Model.ResponseFavorites;
import com.example.android.moviez.Model.UserModel;
import com.example.android.moviez.Model.UserSession;
import com.example.android.moviez.Model.UserToken;
import com.example.android.moviez.Model.VideoModel;
import com.example.android.moviez.Model.WatchlistBody;
import com.example.android.moviez.other.CheckConnection;
import com.example.android.moviez.other.LoggingInterceptor;
import com.example.android.moviez.other.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc on 2/8/2018.
 */

public class RestApi {
    public static final int request_max_time_in_seconds = 20;
    private Context activity;

    public RestApi(Context activity) {
        this.activity = activity;
    }

    public Retrofit getRetrofitInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new RequestInterceptor())
                .readTimeout(request_max_time_in_seconds, TimeUnit.SECONDS)
                .connectTimeout(request_max_time_in_seconds, TimeUnit.SECONDS)
                .build();
        return new  Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
    public ApiService request(){
        return getRetrofitInstance().create(ApiService.class);
    }
    public Call<Model> getNowPlaying (){
        return request().getNowPlaying();
    }
    public  Call<Model> getPopular(){
        return request().getPopular();
    }
    public Call<Model> getTopRated (){
        return  request().getTopRated();
    }
    public Call<Model> getUpcoming (){
        return request().getUpcoming();
    }
    public Call<Model> getSearchedMovie (String term){
        return request().getSearchedMovie(term);
    }
    public Call<PeopleModel> getPeople (){
        return request().getPeople();
    }
    public Call<PeopleModel> getSearchedPeople (String query){
        return request().getSearchedPeople(query);
    }
    public Call<PeopleDetails> getPeopleDetails (int id) {
        return request().getPeopleDetails(id);
    }
    public Call<MovieCredits> getMovieCredits (int id){
        return request().getMovieCredits(id);
    }
    public Call<AuthToken> getToken (){
        return  request().getToken();
    }
    public Call<UserToken> getUserToken (String username, String password, String request_token){
        return request().getUserToken(username,password,request_token);
    }
    public Call<GuestUser> getGuest (){
        return request().getGuest();
    }
    public Call<UserSession> getUserSession(String request_token){
        return request().getUserSession(request_token);
    }
    public Call<Model> getFavorites(String session_id){
        return request().getFavorites(session_id);
    }
    public Call<ResponseFavorites> postFavorites (String session_id, FavoriteBody movie){
        return request().postFavorites(session_id,movie);
    }
    public Call<Model> getRated (String session_id){
        return request().getRated(session_id);
    }
    public Call<Model> getWatchlist (String session_id){
        return request().getWatchlist(session_id);
    }
    public  Call<ResponseFavorites> postWatchlist (String session_id, WatchlistBody movie){
        return request().postWatchlist(session_id, movie);
    }
    public Call<VideoModel> getVideo (int movie_id){
        return request().getVideo(movie_id);
    }
    public  Call<Model> postRated (int movie_id, RateValue value, String session_id){
        return request().postRated(movie_id,value,session_id);
    }
    public Call<UserModel> getUserModel(String session_id){
        return request().getUserModel(session_id);
    }



    public void checkInternet(Runnable callback){
        if (CheckConnection.CheckInternetConnectivity(activity, true, callback )){
            callback.run();
        }
    }
    public void checkInternet (Runnable callback, boolean showConnectionDialog){
        if (CheckConnection.CheckInternetConnectivity(activity,showConnectionDialog,callback))
            callback.run();
        else {
            Toast.makeText(activity, "Connection failed, please check your connection in settings", Toast.LENGTH_LONG).show();
        }
    }

    public  void  checkInternet (Runnable callback, boolean showConnetionDialog, String message){
        if (CheckConnection.CheckInternetConnectivity(activity,showConnetionDialog,callback))
            callback.run();
        else {
            if (showConnetionDialog)
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            else
                Log.d("Connection failed", "" + message);
        }
    }
}