package com.example.android.moviez.Api;

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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pc on 2/8/2018.
 */

public interface ApiService {
    @GET("movie/now_playing")
    Call<Model> getNowPlaying();

    @GET("movie/popular")
    Call<Model> getPopular();

    @GET("movie/top_rated")
    Call<Model> getTopRated();

    @GET("movie/upcoming")
    Call<Model> getUpcoming();

    @GET("search/movie")
    Call<Model> getSearchedMovie(@Query("query") String term);

    @GET("person/popular")
    Call<PeopleModel> getPeople();

    @GET("search/person")
    Call<PeopleModel> getSearchedPeople(@Query("query") String query);

    @GET("movie/{id}/credits")
    Call<MovieCredits> getMovieCredits(@Path("id") int id);

    @GET("person/{id}")
    Call<PeopleDetails> getPeopleDetails(@Path("id") int id);

    @GET("authentication/token/new")
    Call<AuthToken> getToken();

    @GET("authentication/token/validate_with_login")
    Call<UserToken> getUserToken(@Query("username") String username,
                                 @Query("password") String password,
                                 @Query("request_token") String request_token);

    @GET("authentication/guest_session/new")
    Call<GuestUser> getGuest();

    @GET("authentication/session/new")
    Call<UserSession> getUserSession(@Query("request_token") String request_token);

    @GET("account/{account_id}/favorite/movies")
    Call<Model> getFavorites(@Query("session_id") String session_id);

    @GET("account/{account_id}/rated/movies")
    Call<Model> getRated(@Query("session_id") String session_id);
    @POST("movie/{movie_id}/rating")
    Call<Model> postRated (@Path("movie_id") int movie_id, @Body RateValue value, @Query("session_id") String session_id);

    @GET("movie/{movie_id}/videos")
    Call<VideoModel> getVideo (@Path("movie_id") int movie_id);


    @POST("account/{account_id}/favorite")
    Call<ResponseFavorites> postFavorites(@Query("session_id") String session_id, @Body FavoriteBody movie);

    @POST("account/{account_id}/watchlist")
    Call<ResponseFavorites> postWatchlist(@Query("session_id") String session_id, @Body WatchlistBody movie );

    @GET("account/{account_id}/watchlist/movies")
    Call<Model> getWatchlist (@Query("session_id")String session_id);

    @GET("account")
    Call<UserModel> getUserModel (@Query("session_id") String session_id);



}
