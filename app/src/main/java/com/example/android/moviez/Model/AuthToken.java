package com.example.android.moviez.Model;

/**
 * Created by pc on 2/26/2018.
 */

public class AuthToken {
    boolean success;
    String expires_at;
    String request_token;

    public boolean isSuccess() {
        return success;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public String getRequest_token() {
        return request_token;
    }
}
