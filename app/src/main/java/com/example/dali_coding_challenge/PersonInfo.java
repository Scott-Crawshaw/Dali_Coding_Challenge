package com.example.dali_coding_challenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

public class PersonInfo extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        String name = getIntent().getStringExtra("name");
        String json = getIntent().getStringExtra("name");

        map = findViewById(R.id.mapView);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);

        ImageView picture = findViewById(R.id.imageView);

        String imageURL = getImageUrl(json, name);
        if (imageURL != null) {
            Picasso.get().load(getImageUrl(json, name)).into(picture);
        }
    }

    public String getImageUrl(String json, String name) {
        try {
            JSONArray array = new JSONArray(getIntent().getStringExtra("json"));
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString("name").equals(name)) {
                    return ("https://raw.githubusercontent.com/dali-lab/mappy/gh-pages/" + array.getJSONObject(i).getString("iconUrl"));
                }
            }
        } catch (Exception e) {
            return (null);
        }
        return (null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    protected void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    public void onMapReady(GoogleMap gmap) {
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
