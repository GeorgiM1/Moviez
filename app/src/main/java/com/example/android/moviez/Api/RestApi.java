package com.example.android.moviez.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.moviez.BuildConfig;
import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.PeopleModel;
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