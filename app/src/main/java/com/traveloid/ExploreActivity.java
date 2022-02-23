package com.traveloid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.traveloid.api.FirebaseApi;
import com.traveloid.model.Hike;
import com.traveloid.utils.ImageLoadTask;
import com.traveloid.utils.MapperUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {

    private List<Hike> data;
    private List<Hike> favHikes;
    private List<Hike> popHikes;
    private List<Hike> hikes;

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Show progress bar
        progressBar1 = findViewById(R.id.loading1);
        progressBar2 = findViewById(R.id.loading2);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        getData();
    }

    private void initUI() {
        favHikes = data;
        popHikes = data;
        hikes = data;

        LinearLayout favLinearLayout = findViewById(R.id.fav_horizontal_linear);
        for (Hike favHike : favHikes) {
            favLinearLayout.addView(horizontalViewCard(favHike, favLinearLayout));
        }

        LinearLayout popLinearLayout = findViewById(R.id.pop_horizontal_linear);
        for (Hike popHike : popHikes) {
            popLinearLayout.addView(horizontalViewCard(popHike, popLinearLayout));
        }

        LinearLayout otherLinearLayout = findViewById(R.id.vertical_linear);
        for (Hike hike : hikes) {
            otherLinearLayout.addView(verticalViewCard(hike, otherLinearLayout));
        }

        progressBar1.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
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
                            Toast.makeText(ExploreActivity.this, "Please Restart the App", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private View verticalViewCard(Hike hike, LinearLayout mainLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.item_explore_horizontal, mainLayout, false);
        TextView title = myLayout.findViewById(R.id.title);
        TextView distance = myLayout.findViewById(R.id.distance);
        ImageView img = myLayout.findViewById(R.id.image);

        title.setText(hike.getTitle());
        distance.setText(hike.getDistance());

        // set image
        new ImageLoadTask(hike.getImage(), img).execute();

        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, BlogActivity.class);
                intent.putExtra("hike", MapperUtils.convertToSerializable(hike));
                startActivity(intent);
            }
        });
        return myLayout;
    }

    private View horizontalViewCard(Hike hike, LinearLayout mainLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.item_explore_carousel_card, mainLayout, false);
        TextView title = myLayout.findViewById(R.id.title);
        TextView distance = myLayout.findViewById(R.id.distance);
        ImageView img = myLayout.findViewById(R.id.image);

        title.setText(hike.getTitle());
        distance.setText(hike.getDistance());

        // set image
        new ImageLoadTask(hike.getImage(), img).execute();

        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, BlogActivity.class);
                intent.putExtra("hike", MapperUtils.convertToSerializable(hike));
                startActivity(intent);
            }
        });
        return myLayout;
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
}