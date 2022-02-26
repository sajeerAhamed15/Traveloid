package com.traveloid.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.traveloid.model.User;

public class FirebaseApi {
    public static Task<QuerySnapshot> getAllHikes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("hikes").get();
    }

    public static Task<QuerySnapshot> getUser(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").whereEqualTo("email", email).get();
    }

    public static Task<DocumentReference> saveUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users").add(user);
    }
}
