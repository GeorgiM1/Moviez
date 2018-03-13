package com.example.android.moviez.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.Movie;
import com.example.android.moviez.Model.MovieCredits;
import com.example.android.moviez.Model.UserModel;
import com.example.android.moviez.Model.VideoModel;
import com.example.android.moviez.Model.VideoResults;
import com.example.android.moviez.R;
import com.example.android.moviez.other.Constants;
import com.example.android.moviez.other.PreferencesManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.movie_img_details)
    ImageView movieImg;
    @BindView(R.id.movie_name_details)
    TextView movieName;
    @BindView(R.id.movie_director)
    TextView movieDirector;
    @BindView(R.id.movie_writers)
    TextView movieWriter;
    @BindView(R.id.movie_stars)
    TextView movieStars;
    @BindView(R.id.movie_trailer)
    LinearLayout movieTrailer;
    DrawerLayout drawer;
    @BindView(R.id.movie_description)
    TextView movieDescription;
    @BindView(R.id.writers)
    TextView writerText;
    @BindView(R.id.director)
    TextView directorText;
    @BindView(R.id.stars)
    TextView starsText;
    Movie movie;
    RestApi api;
    MovieCredits credits;
    VideoModel video = new VideoModel();
    String key;
    VideoResults videoResults = new VideoResults();
    UserModel userModel;
    @BindView(R.id.cast_crew_btn)
    Button detailsBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setTitle("Details");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        api = new RestApi(this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final String session_id = PreferencesManager.getSessionId(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                            Picasso.with(MovieDetailsActivity.this).load("https://secure.gravatar.com/avatar/"+hash).centerCrop().fit().into(imageView);
                            name.setText(userModel.getName());

                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                        }
                    });
                }
            });

        }

        movie = PreferencesManager.getMovie(this);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieDetailsActivity.this, CastAndCrewActivity.class));

            }
        });
        Picasso.with(this).load(Constants.imgLink + movie.getPoster_path()).centerCrop().fit().into(movieImg);
        movieName.setText(movie.getOriginal_title());
        movieDescription.setText(movie.getOverview());
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<MovieCredits> call = api.getMovieCredits(movie.getId());
                call.enqueue(new Callback<MovieCredits>() {
                    @Override
                    public void onResponse(Call<MovieCredits> call, Response<MovieCredits> response) {
                        credits = response.body();
                        for (int i = 0; i < credits.getCrew().size() && i < credits.getCast().size(); i++) {
                            if (credits.getCrew().get(i).getJob().equals("Director")) {
                                if (movieDirector.getText().equals("")) {
                                    movieDirector.setText(movieDirector.getText() + credits.getCrew().get(i).getName());
                                } else
                                    movieDirector.setText(movieDirector.getText() + ", " + credits.getCrew().get(i).getName());
                            }
                            if (credits.getCrew().get(i).getJob().equals("Writer")) {

                                if (movieWriter.getText().equals("")) {
                                    movieWriter.setText(movieWriter.getText() + credits.getCrew().get(i).getName());
                                } else
                                    movieWriter.setText(movieWriter.getText() + ", " + (credits.getCrew().get(i).getName()));
                            }
                            if (movieStars.getText().equals("")) {
                                movieStars.setText(movieStars.getText() + credits.getCast().get(i).getName());
                            } else
                                movieStars.setText(movieStars.getText() + ", " + credits.getCast().get(i).getName());
                        }
                        hideTxt();
                    }

                    @Override
                    public void onFailure(Call<MovieCredits> call, Throwable t) {

                    }
                });
            }
        });
        api.checkInternet(new Runnable() {
            @Override
            public void run() {
                Call<VideoModel> call = api.getVideo(movie.getId());
                call.enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        video=response.body();

                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {

                    }
                });
            }
        });
        movieTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoResults= video.getResults().get(0);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoResults.getKey()));
                startActivity(intent);
            }
        });





    }
    public void hideTxt (){
        if (movieWriter.getText().equals("")) {
            movieWriter.setVisibility(View.GONE);
            writerText.setVisibility(View.GONE);
        }
        if (movieDirector.getText().equals("")) {
            movieDirector.setVisibility(View.GONE);
            directorText.setVisibility(View.GONE);
        }
        if (movieStars.getText().equals("")) {
            movieStars.setVisibility(View.GONE);
            starsText.setVisibility(View.GONE);
        }
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

            startActivity(new Intent( this, FavoritesActivity.class));
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

            startActivity(new Intent(this, LoginActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}

