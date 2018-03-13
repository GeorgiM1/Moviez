package com.example.android.moviez.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.moviez.Api.RestApi;
import com.example.android.moviez.Model.AuthToken;
import com.example.android.moviez.Model.UserSession;
import com.example.android.moviez.Model.UserToken;
import com.example.android.moviez.R;
import com.example.android.moviez.other.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.username_edittext)
    TextView username;
    @BindView(R.id.password_edittext)
    TextView password;
    @BindView(R.id.login_btn)
    Button loginBtn;
    RestApi api;
    String mAuthTokenString;
    AuthToken mToken;
    UserSession mUserSession;
    UserToken mUserToken;
    String mSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        api=new RestApi(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.checkInternet(new Runnable() {
                    @Override
                    public void run() {
                        Call<AuthToken> call = api.getToken();
                        call.enqueue(new Callback<AuthToken>() {
                            @Override
                            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                mToken= response.body();
                                mAuthTokenString = mToken.getRequest_token();
                                Call<UserToken> userCall = api.getUserToken(username.getText().toString(), password.getText().toString(),mAuthTokenString);
                                userCall.enqueue(new Callback<UserToken>() {
                                    @Override
                                    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                                        mUserToken = response.body();
                                        Call<UserSession> sessionCall = api.getUserSession(mAuthTokenString);
                                        sessionCall.enqueue(new Callback<UserSession>() {
                                            @Override
                                            public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                                                mUserSession = response.body();
                                                mSessionId = mUserSession.getSession_id();
                                                PreferencesManager.addSessionId(mSessionId, LoginActivity.this);
                                                if (response.isSuccessful()) {
                                                    startActivity(new Intent(LoginActivity.this, ExploreActivity.class));
                                                }



                                            }

                                            @Override
                                            public void onFailure(Call<UserSession> call, Throwable t) {

                                            }
                                        });




                                    }

                                    @Override
                                    public void onFailure(Call<UserToken> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<AuthToken> call, Throwable t) {

                            }
                        });
                    }
                });

            }
        });
    }

    }


