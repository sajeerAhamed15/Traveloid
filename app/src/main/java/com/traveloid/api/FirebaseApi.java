package com.traveloid.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseApi {
    public static Task<QuerySnapshot> getAllHikes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("hikes").get();
    }
}
