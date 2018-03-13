package com.example.android.moviez.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviez.Adapters.ItemAdapter;
import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.Model;
import com.example.android.moviez.Model.UserModel;
import com.example.android.moviez.R;
import com.example.android.moviez.other.PreferencesManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    ItemAdapter itemAdapter;
    Model model;
    Model favMovies;
    Model watchlistMovies;
    RestApi api;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    DrawerLayout drawer;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipeToRefresh;
    @BindView(R.id.Search_bar)
    EditText mSearchBar;
    String session_id;
    MenuItem menuItem;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        favMovies =PreferencesManager.getFavMovie(this);
        api = new RestApi(this);

        watchlistMovies = PreferencesManager.getWatchlistMovies(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToRefresh.setRefreshing(true);
                refreshRecycle();
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
                        Call<Model> call = api.getFavorites(session_id);
                        call.enqueue(new Callback<Model>() {
                            @Override
                            public void onResponse(Call<Model> call, Response<Model> response) {
                                model = response.body();
                                itemAdapter = new ItemAdapter(FavoritesActivity.this, model.getResults(), new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshData();
                                    }
                                });
                                itemAdapter.setWatchlistBody(watchlistMovies);
                                if (mSearchBar.getText().length()>=3) {
                                    recyclerView.setAdapter(itemAdapter);
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


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        session_id = PreferencesManager.getSessionId(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        View view = navigationView.getHeaderView(0);
        final ImageView imageView =(ImageView) view.findViewById(R.id.imageView_drawer);
        final TextView name = (TextView)view.findViewById(R.id.text_drawe);


        if (session_id.length()>3){
            api.checkInternet(new Runnable() {
                @Override
                public void run() {
                    Call<UserModel> call = api.getUserModel(session_id);
                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            userModel=response.body();
                            String hash = userModel.getAvatar().getGravatar().getHash();
                            Picasso.with(FavoritesActivity.this).load("https://secure.gravatar.com/avatar/"+hash).centerCrop().fit().into(imageView);
                            name.setText(userModel.getName());

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                        }
                    });
                }
            });

        }

        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<Model> call = api.getFavorites(session_id);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        model = response.body();
                        itemAdapter = new ItemAdapter(FavoritesActivity.this, model.getResults(), new Runnable() {
                            @Override
                            public void run() {
                                refreshData();
                            }
                        });
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
        menuItem = navigationView.getMenu().findItem(R.id.login);
        if (session_id.equals("")){
            menuItem.setTitle("Login");
        }else menuItem.setTitle("Logout");




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.explore) {
            startActivity(new Intent(this, ExploreActivity.class));
          finish();
            // Handle the camera action
        } else if (id == R.id.favorites) {

            drawer.closeDrawers();

        } else if (id == R.id.rated) {


            Intent intent = new Intent(this, RatedActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.watchlist) {

            Intent intent = new Intent(this, WatchlistActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.people) {

            Intent intent = new Intent(this, PeopleActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.login) {
            startActivity(new Intent( this, LoginActivity.class));

            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void refreshRecycle (){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<Model> call = api.getFavorites(session_id);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        model = response.body();
                        itemAdapter = new ItemAdapter(FavoritesActivity.this, model.getResults(), new Runnable() {
                            @Override
                            public void run() {
                                refreshData();
                            }
                        });
                        itemAdapter.setWatchlistBody(watchlistMovies);
                        recyclerView.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();
                        mSwipeToRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        mSwipeToRefresh.setRefreshing(false);

                    }
                });
            }
        });

    }
    public void  refreshData (){
        model= PreferencesManager.getFavMovie(this);
        itemAdapter= new ItemAdapter(FavoritesActivity.this, model.getResults(), new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });
        itemAdapter.setFav(favMovies);
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }





}

