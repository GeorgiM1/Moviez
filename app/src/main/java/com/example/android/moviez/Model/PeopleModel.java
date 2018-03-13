package com.example.android.moviez.Model;

import java.util.ArrayList;

/**
 * Created by pc on 2/10/2018.
 */

public class PeopleModel {
    ArrayList<People> results;

    public ArrayList<People> getResults() {
        return results;
    }

    public boolean hasMovie(String id) {
        for (int i = 0; i < results.size(); i++) {
            if (id.equals(results.get(i).getId())) {
                return true;
            }
        }
        return false;
    }
    public int getPosition(String id) {

        for (int i = 0; i < results.size(); i++) {
            if (id.equals(results.get(i).getId())) {
                return i;
            }
        }
        return 0;
    }


}
