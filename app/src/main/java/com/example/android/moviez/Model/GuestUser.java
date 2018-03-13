package com.example.android.moviez.Model;

/**
 * Created by pc on 2/26/2018.
 */

public class GuestUser {

    boolean success;

    String guest_session_id;

    String expires_at;

    public boolean isSuccess() {
        return success;
    }

    public String getGuest_session_id() {
        return guest_session_id;
    }

    public String getExpires_at() {
        return expires_at;
    }
}
