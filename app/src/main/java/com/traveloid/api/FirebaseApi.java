package com.traveloid.api;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.traveloid.model.Hike;
import com.traveloid.model.User;

import java.util.UUID;

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

    public static Task<DocumentReference> saveHike(Hike hike) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("hikes").add(hike);
    }

    public static StorageReference saveProfilePicture() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("profile-pics/" + UUID.randomUUID().toString());
        return ref;
    }
}
