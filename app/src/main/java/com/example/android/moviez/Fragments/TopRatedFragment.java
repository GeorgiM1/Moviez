package com.example.android.moviez.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.android.moviez.Adapters.ItemAdapter;
import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.Model;
import com.example.android.moviez.R;
import com.example.android.moviez.other.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pc on 2/6/2018.
 */

public class TopRatedFragment extends Fragment {
    Unbinder unbinder;
    ItemAdapter itemAdapter;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    Model model;
    Model favMovies;
    Model watchlistMovies;
    RestApi api;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.Search_bar)
    EditText mSearchBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_fragment_layout, null);
        unbinder = ButterKnife.bind(this, view);
        favMovies= PreferencesManager.getFavMovie(getActivity());
        watchlistMovies= PreferencesManager.getWatchlistMovies(getActivity());
        api = new RestApi(getActivity());



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshRecycleView();

            }
        });
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                api.checkInternet(new Runnable() {
                    @Override
                    public void run() {
                        retrofit2.Call<Model> call = api.getSearchedMovie(mSearchBar.getText().toString());
                        call.enqueue(new Callback<Model>() {
                            @Override
                            public void onResponse(Call<Model> call, Response<Model> response) {

                                if (response.isSuccessful()){
                                    model=response.body();
                                    itemAdapter = new ItemAdapter(getActivity(), model.getResults());
                                    itemAdapter.setFav(favMovies);
                                    itemAdapter.setWatchlistBody(watchlistMovies);
                                    if (mSearchBar.getText().length() >= 3){
                                        recyclerView.setAdapter(itemAdapter);
                                    }
                                    itemAdapter.notifyDataSetChanged();

                                }



                            }

                            @Override
                            public void onFailure(Call<Model> call, Throwable t) {

                            }
                        });
                    }
                });

            }
        });

        model = new Model();


        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                retrofit2.Call<Model> call = api.getTopRated();
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {

                        model=response.body();
                        itemAdapter = new ItemAdapter(getActivity(), model.getResults());
                        itemAdapter.setFav(favMovies);
                        itemAdapter.setWatchlistBody(watchlistMovies);
                        recyclerView.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                    }
                });
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    public void refreshRecycleView (){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                retrofit2.Call<Model> call = api.getTopRated();
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {

                        model=response.body();
                        itemAdapter = new ItemAdapter(getActivity(), model.getResults());
                        itemAdapter.setFav(favMovies);
                        itemAdapter.setWatchlistBody(watchlistMovies);
                        recyclerView.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        mSwipeRefreshLayout.setRefreshing(false);


                    }
                });
            }
        });


    }


}