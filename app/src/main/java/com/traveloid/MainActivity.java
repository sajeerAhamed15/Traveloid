package com.traveloid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.traveloid.model.Hike;
import com.traveloid.adapter.CarouselAdapter;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<CarouselAdapter.ViewHolder> {

    private List<Hike> data;

    private TextView currentItemName;
    private TextView currentItemDistance;
    private DiscreteScrollView itemPicker;
    private InfiniteScrollAdapter<?> infiniteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentItemName = findViewById(R.id.item_name);
        currentItemDistance = findViewById(R.id.item_distance);
        data = getData();

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

        hideSystemBars();
    }


    private void onItemChanged(Hike hike) {
        currentItemName.setText(hike.getName());
        currentItemDistance.setText(hike.getDistance());
    }

    private List<Hike> getData() {
        return Arrays.asList(
                new Hike(1, "The Hadrian's Wall Path", "5 miles", R.drawable.hike2),
                new Hike(2, "South West Coast Path", "12 miles", R.drawable.hike7),
                new Hike(3, "Steep Cliffs of The Quiraing", "8 miles", R.drawable.hike3),
                new Hike(4, "The South Downs Way", "15 miles", R.drawable.hike4),
                new Hike(5, "Scafell Pike", "8 miles", R.drawable.hike5));
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