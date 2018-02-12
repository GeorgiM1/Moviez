package com.example.android.moviez.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.view.WindowManager;
import android.widget.EditText;

import com.example.android.moviez.Adapters.PeopleAdapter;
import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.PeopleModel;
import com.example.android.moviez.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

   PeopleAdapter mPeopleAdapter;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    DrawerLayout drawer;
    TabLayout tabLayout;
    RestApi api;
    PeopleModel peopleModel;
    @BindView(R.id.Search_bar)
    EditText mSearchBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipeRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setTitle("People");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        api = new RestApi(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
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
                        retrofit2.Call<PeopleModel> call = api.getSearchedPeople(mSearchBar.getText().toString());
                        call.enqueue(new Callback<PeopleModel>() {
                            @Override
                            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                                if (response.isSuccessful()){
                                    peopleModel=response.body();
                                    mPeopleAdapter = new PeopleAdapter(PeopleActivity.this, peopleModel.getResults());
                                    if (mSearchBar.getText().length() >= 3){
                                        recyclerView.setAdapter(mPeopleAdapter);
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<PeopleModel> call, Throwable t) {

                            }
                        });

                    }
                });

            }
        });

        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                retrofit2.Call<PeopleModel> call = api.getPeople();
                call.enqueue(new Callback<PeopleModel>() {
                    @Override
                    public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                        peopleModel = response.body();
                        mPeopleAdapter = new PeopleAdapter(PeopleActivity.this, peopleModel.getResults());
                        recyclerView.setAdapter(mPeopleAdapter);



                    }

                    @Override
                    public void onFailure(Call<PeopleModel> call, Throwable t) {

                    }
                });

            }
        });



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
            drawer.closeDrawers();
        } else if (id == R.id.people) {

            Intent intent = new Intent(this, PeopleActivity.class);
            startActivity(intent);
            drawer.closeDrawers();

        } else if (id == R.id.login) {

            drawer.closeDrawers();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void refreshRecycleView (){
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                retrofit2.Call<PeopleModel> call = api.getPeople();
                call.enqueue(new Callback<PeopleModel>() {
                    @Override
                    public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                        peopleModel = response.body();
                        mPeopleAdapter = new PeopleAdapter(PeopleActivity.this, peopleModel.getResults());
                        recyclerView.setAdapter(mPeopleAdapter);



                    }

                    @Override
                    public void onFailure(Call<PeopleModel> call, Throwable t) {

                    }
                });

            }
        });

    }





}
