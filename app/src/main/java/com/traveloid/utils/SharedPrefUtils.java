package com.traveloid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.traveloid.model.Hike;
import com.traveloid.model.User;

import java.util.List;

public class SharedPrefUtils {
    public static User saveUserInSP(User user, Context context) {
        SharedPreferences pref = context.getSharedPreferences("TraveloidPrefs", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString("user", json);
        editor.commit();

        return user;
    }

    public static User getUserFromSP(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("TraveloidPrefs", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");

        if (json.equals("")) return null;

        return gson.fromJson(json, User.class);
    }
}
