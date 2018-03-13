package com.example.android.moviez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.moviez.Activity.MovieDetailsActivity;
import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.FavoriteBody;
import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.Movie;
import com.example.android.moviez.Model.RateValue;
import com.example.android.moviez.Model.ResponseFavorites;
import com.example.android.moviez.Model.WatchlistBody;
import com.example.android.moviez.R;
import com.example.android.moviez.other.Constants;
import com.example.android.moviez.other.PreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movies;
    RestApi api;
    RateValue rateValue;
    private Model fav = new Model();
    FavoriteBody favMovie = new FavoriteBody();
    WatchlistBody watchlistBody = new WatchlistBody();
    private Model watchlisted = new Model();
    private Model rated = new Model();
    private String session_id;
    Runnable runnable;


    public ItemAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }
    public ItemAdapter(Context context, ArrayList<Movie> movies, Runnable runnable) {
        this.context = context;
        this.movies = movies;
        this.runnable=runnable;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        session_id = PreferencesManager.getSessionId(context);
        api = new RestApi(context);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, final int position) {
        final Movie movie = movies.get(position);

        Picasso.with(context).load(Constants.imgLink + movie.getPoster_path()).into(holder.movieImg);
        holder.movieName.setText(movie.getTitle());
        holder.movieRating.setText(Double.toString(movie.getVote_average()));


        holder.movieRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateValue = new RateValue();
                holder.ratingBar.setVisibility(View.VISIBLE);




                holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                        rateValue.setValue(ratingBar.getRating()*2);
                        Call<Model> call = api.postRated(movie.getId(), rateValue, session_id);
                        call.enqueue(new Callback<Model>() {
                            @Override
                            public void onResponse(Call<Model> call, Response<Model> response) {
                                Call<Model> call1 = api.getRated(session_id);
                                call1.enqueue(new Callback<Model>() {
                                    @Override
                                    public void onResponse(Call<Model> call, Response<Model> response) {
                                        holder.movieRating.setText(Double.toString(movie.getVote_average()));
                                    }

                                    @Override
                                    public void onFailure(Call<Model> call, Throwable t) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<Model> call, Throwable t) {

                            }
                        });

                    }
                });
            }
        });

        if (isWatchlist(movie.getId())) {
            holder.movieWatchlist.setText("Added to watchlist");
            holder.movieWatchlist.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.watchlist_remove), null,null,null);
        } else{ holder.movieWatchlist.setText("Add to watchlist");
        holder.movieWatchlist.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.watchlist_add),null,null,null);
        }

        holder.movieWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.movieWatchlist.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (context.getResources().getDrawable(isWatchlist(movie.getId())
                                ? R.drawable.watchlist_add : R.drawable.watchlist_remove), null, null, null);
                if (isWatchlist(movie.getId())) {
                    holder.movieWatchlist.setText("Add to watchlist");
                } else holder.movieWatchlist.setText("Added to watchlist");


                watchlistBody.setMedia_id(movie.getId());
                watchlistBody.setMedia_type("movie");
                watchlistBody.setWatchlist(!isWatchlist(movie.getId()));
                api.checkInternet(new Runnable() {
                    @Override
                    public void run() {
                        Call<ResponseFavorites> call = api.postWatchlist(session_id, watchlistBody);
                        call.enqueue(new Callback<ResponseFavorites>() {
                            @Override
                            public void onResponse(Call<ResponseFavorites> call, Response<ResponseFavorites> response) {
                                Call<Model> modelCall = api.getWatchlist(session_id);
                                modelCall.enqueue(new Callback<Model>() {
                                    @Override
                                    public void onResponse(Call<Model> call, Response<Model> response) {
                                        watchlisted = response.body();
                                        PreferencesManager.addWatchlistMovies(watchlisted, context);
                                        notifyDataSetChanged();
                                        if (runnable!= null){
                                            runnable.run();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Model> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseFavorites> call, Throwable t) {

                            }
                        });
                    }
                });


            }
        });
        holder.movieName.setCompoundDrawablesRelativeWithIntrinsicBounds
                (context.getResources().getDrawable(isFav(movie.getId()) ? R.drawable.favourites_full : R.drawable.favourites_empty), null, null, null);
        holder.movieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.movieName.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (context.getResources().getDrawable(isFav(movie.getId()) ? R.drawable.favourites_empty : R.drawable.favourites_full), null, null, null);

                favMovie.setFavorite(!isFav(movie.getId()));
                favMovie.setMedia_id(movie.getId());
                favMovie.setMedia_type("movie");
                api.checkInternet(new Runnable() {
                    @Override
                    public void run() {
                        Call<ResponseFavorites> call = api.postFavorites(session_id, favMovie);
                        call.enqueue(new Callback<ResponseFavorites>() {
                            @Override
                            public void onResponse(Call<ResponseFavorites> call, Response<ResponseFavorites> response) {
                                Call<Model> call1 = api.getFavorites(session_id);
                                call1.enqueue(new Callback<Model>() {
                                    @Override
                                    public void onResponse(Call<Model> call, Response<Model> response) {
                                        fav = response.body();
                                        PreferencesManager.addFavMovie(fav, context);
                                        if (runnable!= null){
                                            runnable.run();
                                        }
                                        notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFailure(Call<Model> call, Throwable t) {

                                    }
                                });


                            }

                            @Override
                            public void onFailure(Call<ResponseFavorites> call, Throwable t) {

                            }
                        });
                    }
                });


            }
        });


        holder.movieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                PreferencesManager.addMovie(movie, context);
                context.startActivity(intent);
            }
        });


    }

    public boolean isFav(int id) {
        Model model = PreferencesManager.getFavMovie(context);
        if (model!=null) {
            ArrayList<Movie> results = model.getResults();
            if (results != null)
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getId() == id) {
                        return true;
                    }
                }
        }
        return false;
    }

    public boolean isWatchlist(int id) {
        Model model = PreferencesManager.getWatchlistMovies(context);

        if (model!=null) {
            ArrayList<Movie> results = model.getResults();
            if (results != null)
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getId() == id) {
                        return true;
                    }
                }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setFav(Model fav) {
        this.fav = fav;
    }

    public void setWatchlistBody(Model watchlisted) {
        this.watchlisted = watchlisted;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        ImageView movieImg;
        @BindView(R.id.movie_nameTXT)
        TextView movieName;
        @BindView(R.id.movie_ratingTXT)
        TextView movieRating;
        @BindView(R.id.movie_watchlistTXT)
        TextView movieWatchlist;
        @BindView(R.id.star_rating_bar)
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
