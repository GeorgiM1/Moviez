package com.example.android.moviez.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviez.Model.Movie;
import com.example.android.moviez.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc on 2/6/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movies;


    public ItemAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout,parent, false);
        ViewHolder viewHolder = new ViewHolder (view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Picasso.with(context).load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path()).into(holder.movieImg);
        holder.movieName.setText(movie.getTitle().toString());
        holder.movieRating.setText(Double.toString(movie.getVote_average()));
        holder.movieWatchlist.setText("20");


    }

    @Override
    public int getItemCount() {
        return movies.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
