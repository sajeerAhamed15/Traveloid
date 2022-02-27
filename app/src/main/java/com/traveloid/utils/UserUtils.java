package com.traveloid.utils;

import com.traveloid.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    public static User updateLikes(String hikeTitle, User user) {
        List<String> favHikes;
        if (user.getFavorites() == null) {
            favHikes = new ArrayList<>();
        } else {
            favHikes = user.getFavorites();
        }
        if (favHikes.contains(hikeTitle)) {
            return user;
        }
        favHikes.add(hikeTitle);
        user.setFavorites(favHikes);
        return user;
    }

    public static User updateDislikes(String hikeTitle, User user) {
        List<String> favHikes;
        if (user.getFavorites() == null) {
            favHikes = new ArrayList<>();
        } else {
            favHikes = user.getFavorites();
        }
        if (!favHikes.contains(hikeTitle)) {
            return user;
        }
        favHikes.remove(hikeTitle);
        user.setFavorites(favHikes);
        return user;
    }
}
