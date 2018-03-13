package com.example.android.moviez.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.People;
import com.example.android.moviez.Model.PeopleDetails;
import com.example.android.moviez.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2/10/2018.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    Context context;
    ArrayList<People> people;
    RestApi api;
    PeopleDetails peopleDetails;


    public PeopleAdapter(Context context, ArrayList<People> people) {
        this.context = context;
        this.people = people;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.people_adapter_layout,parent, false);
        PeopleAdapter.ViewHolder viewHolder = new PeopleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final People _people = people.get(position);
        holder.peopleName.setText(_people.getName().toString());
        holder.peopleRating.setText(Double.toString(_people.getPopularity()));
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + _people.getProfile_path()).into(holder.peopleImg);
        for (int i = 0; i < _people.getKnown_for().size(); i++) {
            if (holder.knownFor.getText().equals("")) {
                holder.knownFor.setText(holder.knownFor.getText() + _people.getKnown_for().get(i).getTitle());

            }
            holder.knownFor.setText(holder.knownFor.getText() + ", " + _people.getKnown_for().get(i).getTitle());
        }
        api = new RestApi(context);
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<PeopleDetails> call = api.getPeopleDetails(_people.getId());
                call.enqueue(new Callback<PeopleDetails>() {
                    @Override
                    public void onResponse(Call<PeopleDetails> call, Response<PeopleDetails> response) {
                        peopleDetails = response.body();
                        if (peopleDetails.getBiography()==null){
                            holder.peopleDetails.setText("");
                        }else holder.peopleDetails.setText(peopleDetails.getBiography());

                    }

                    @Override
                    public void onFailure(Call<PeopleDetails> call, Throwable t) {

                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.people_img)
        ImageView peopleImg;
        @BindView(R.id.people_rating)
        TextView peopleRating;
        @BindView(R.id.people_bioTXT)
        TextView knownFor;
        @BindView(R.id.people_name)
        TextView peopleName;
        @BindView(R.id.people_details)
        TextView peopleDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

