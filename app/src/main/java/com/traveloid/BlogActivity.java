package com.traveloid;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.traveloid.model.Hike;
import com.traveloid.model.HikeSerializable;
import com.traveloid.utils.ImageLoadTask;
import com.traveloid.utils.MapperUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlogActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    List<LatLng> path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        TextView title = findViewById(R.id.title);
        TextView distance = findViewById(R.id.distance);
        ImageView img = findViewById(R.id.image);

        HikeSerializable hike = (HikeSerializable) getIntent().getSerializableExtra("hike");
        if (hike == null) {
            Intent intent = new Intent(BlogActivity.this, ExploreActivity.class);
            startActivity(intent);
        } else {
            path = MapperUtils.convertToLatLang(hike.getPath());
            title.setText(hike.getTitle());
            distance.setText(hike.getDistance());
            // set image
            new ImageLoadTask(hike.getImage(), img).execute();

            // set image
            try {
                URL url = new URL(hike.getImage());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (Exception ignored) {}

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("HikePath", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("HikePath", "Can't find style. Error: ", e);
        }


        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(path));

        stylePolyline(polyline1);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(path.get(0), 5));

        googleMap.setOnPolylineClickListener(this);
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private void stylePolyline(Polyline polyline) {
        polyline.setStartCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_adjust_black), 10));

        polyline.setEndCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_bullseye_black), 10));
        polyline.setWidth(10);
        polyline.setColor(Color.BLACK);

        PatternItem DOT = new Dot();
        PatternItem GAP = new Gap(20);
        List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

        polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        polyline.setJointType(JointType.ROUND);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}