package com.traveloid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.traveloid.api.FirebaseApi;
import com.traveloid.model.Hike;
import com.traveloid.adapter.CarouselAdapter;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<CarouselAdapter.ViewHolder> {

    private List<Hike> data;

    private TextView currentItemName;
    private TextView currentItemDistance;
    private DiscreteScrollView itemPicker;
    private InfiniteScrollAdapter<?> infiniteAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentItemName = findViewById(R.id.item_name);
        currentItemDistance = findViewById(R.id.item_distance);

        // Show progress bar
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);

        // get all hikes from Firebase
        getData();

        // To hide the navigation bar and status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        hideSystemBars();
    }

    private List<Hike> getDummyData() {
        String link = "https://images.unsplash.com/photo-1586022045497-31fcf76fa6cc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTN8fGhpa2luZ3xlbnwwfHwwfHw%3D&w=1000&q=80";
        return Arrays.asList(
                new Hike("1", "The Hadrian's Wall Path", "5 miles", link, getDummyPath(), true, true),
                new Hike("2", "South West Coast Path", "12 miles", link, getDummyPath(), true, true),
                new Hike("3", "Steep Cliffs of The Quiraing", "8 miles", link, getDummyPath(), true, true),
                new Hike("4", "The South Downs Way", "15 miles", link, getDummyPath(), true, true),
                new Hike("5", "Scafell Pike", "8 miles", link, getDummyPath(), true, true));
    }
    private List<GeoPoint> getDummyPath() {
        return Arrays.asList(
                new GeoPoint(-32, 143.321),
                new GeoPoint(-34.747, 145.592),
                new GeoPoint(-34.364, 147.891),
                new GeoPoint(-33.501, 150.217),
                new GeoPoint(-32.306, 149.248),
                new GeoPoint(-32.491, 147.309)
        );
    }

    private void initUI() {
        itemPicker = findViewById(R.id.item_picker);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new CarouselAdapter(data));
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        onItemChanged(data.get(0));

        progressBar.setVisibility(View.GONE);
    }


    private void onItemChanged(Hike hike) {
        currentItemName.setText(hike.getTitle());
        currentItemDistance.setText(hike.getDistance());
    }

    private void getData() {
        FirebaseApi.getAllHikes().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Hike> hikes = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        hikes.add(document.toObject(Hike.class));
                    }
                    data = hikes;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initUI();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Please Restart the App", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onCurrentItemChanged(@Nullable CarouselAdapter.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onItemChanged(data.get(positionInDataSet));
    }

    public void exploreClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
        startActivity(intent);
    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }
}