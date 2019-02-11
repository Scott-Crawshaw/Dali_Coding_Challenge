package com.example.dali_coding_challenge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

public class PersonInfo extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView map;
    private String json;
    private int index;
    private LatLng coords;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        index = getIntent().getIntExtra("index", 0);
        json = getIntent().getStringExtra("json");

        getSupportActionBar().setTitle(getName(index));

        map = findViewById(R.id.mapView);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);

        //load image into image view using Picasso
        ImageView picture = findViewById(R.id.imageView);

        String imageURL = getImageUrl(index);
        if (imageURL != null) {
            Picasso.get().load(imageURL).into(picture);
        }

        //load in message
        TextView message = findViewById(R.id.textView);
        message.setText(getMessage(index));

        //establish coordinates for callback use
        setCoords(index);

        //establish URL for callback use
        setURL(index);
    }

    public void onClick(View view) {
        //navigate to person's URL if it exists
        if (url != null) {
            if (url.substring(0, 2).equals("//")) {
                url = "https://" + url.substring(2);
            } else if (!url.contains("https://")) {
                url = "https://raw.githubusercontent.com/dali-lab/mappy/gh-pages/" + url;
            }

            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {

            }

        }

    }

    private void setURL(int index) {
        //gets the url of the person at a given index in the JSONArray
        try {
            JSONArray array = new JSONArray(json);
            url = array.getJSONObject(index).getString("url");
        } catch (Exception e) {
            url = null;
        }
    }

    private String getName(int index) {
        //returns the name of the person at a given index in the JSONArray
        try {
            JSONArray array = new JSONArray(json);
            return (array.getJSONObject(index).getString("name"));
        } catch (Exception e) {
            return ("");
        }

    }

    private String getImageUrl(int index) {
        //returns the iconUrl of the person at a given index in the JSONArray
        try {
            JSONArray array = new JSONArray(json);
            return ("https://raw.githubusercontent.com/dali-lab/mappy/gh-pages/" + array.getJSONObject(index).getString("iconUrl"));
        } catch (Exception e) {
            return (null);
        }

    }

    private String getMessage(int index) {
        //returns the message of the person at a given index in the JSONArray
        try {
            JSONArray array = new JSONArray(json);
            return (array.getJSONObject(index).getString("message"));
        } catch (Exception e) {
            return ("");
        }
    }

    private void setCoords(int index) {
        //gets the coordinates of the person at a given index in the JSONArray and sets them to a global variable
        try {
            JSONArray array = new JSONArray(json);
            double lat = array.getJSONObject(index).getJSONArray("lat_long").getDouble(0);
            double lng = array.getJSONObject(index).getJSONArray("lat_long").getDouble(1);
            coords = new LatLng(lat, lng);
        } catch (Exception e) {
            coords = new LatLng(43.7044, -72.2887);
        }

    }


    /* BELOW ARE REQUIRED METHODS FOR THE IMPLEMENTED CLASS*/

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
        //focus the camera on the hometown and drop a pin there
        gmap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        MarkerOptions marker = new MarkerOptions();
        marker.position(coords);
        gmap.addMarker(marker);
    }
}
