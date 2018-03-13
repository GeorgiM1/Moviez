package com.example.android.moviez.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviez.Adapters.ViewPagerAdapter;
import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Fragments.NowPlayingFragment;
import com.example.android.moviez.Fragments.PopularFragment;
import com.example.android.moviez.Fragments.TopRatedFragment;
import com.example.android.moviez.Fragments.UpcomingFragment;
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

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    DrawerLayout drawer;
    String session_id;
    MenuItem menuItem;
    RestApi api;
    Model model;
    Model watchlistMovies;
    UserModel  userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setViewPagerAdapter(pager);
        tabLayout.setupWithViewPager(pager);
        api= new RestApi(this);

        session_id = PreferencesManager.getSessionId(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Explore");
        menuItem = navigationView.getMenu().findItem(R.id.login);
        if (session_id.equals("")){
            menuItem.setTitle("Login");
        }else menuItem.setTitle("Logout");
        View view = navigationView.getHeaderView(0);
        final ImageView imageView =(ImageView) view.findViewById(R.id.imageView_drawer);
        final TextView name = (TextView)view.findViewById(R.id.text_drawe);
        final TextView user = (TextView) view.findViewById(R.id.textView_email);

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
                            Picasso.with(ExploreActivity.this).load("https://secure.gravatar.com/avatar/"+hash).centerCrop().fit().into(imageView);
                            name.setText(userModel.getName());
                            user.setText(userModel.getUsername());

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
                        model=response.body();
                        PreferencesManager.addFavMovie(model,ExploreActivity.this);
                    }
                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                    }
                });
            }
        });
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<Model> call = api.getWatchlist(session_id);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        watchlistMovies = response.body();
                        PreferencesManager.addWatchlistMovies(watchlistMovies, ExploreActivity.this);
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

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
            drawer.closeDrawers();
            // Handle the camera action
        } else if (id == R.id.favorites) {

            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            finish();

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

            Intent intent = new Intent( this, LoginActivity.class);
            startActivity(intent);
           finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void setViewPagerAdapter (ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NowPlayingFragment(), "Now Playing");
        viewPagerAdapter.addFragment(new PopularFragment(), "Popular");
        viewPagerAdapter.addFragment(new TopRatedFragment(), "Top Rated");
        viewPagerAdapter.addFragment(new UpcomingFragment(), "Upcoming");
        pager.setAdapter(viewPagerAdapter);
    }

}
