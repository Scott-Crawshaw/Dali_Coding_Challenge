package com.example.dali_coding_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hometowns extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView map;
    private String json_string;
    private Map<String, Integer> markerTags = new HashMap<String, Integer>(); //for connecting marker ids to people indexes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hometowns);
        getSupportActionBar().setTitle("Geography");

        getJsonString();

        //initialize the map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        map = findViewById(R.id.mapView2);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);
    }

    private void getJsonString() {
        //download json from members.json and save it in global variable
        json_string = "";

        //Allow for non-async connection, which is acceptable in this context as the map is useless without data
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("https://raw.githubusercontent.com/dali-lab/mappy/gh-pages/members.json");
            InputStream stream;
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line = null;

            while ((line = reader.readLine()) != null) {
                json_string += line;
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            //empty json array
            json_string = "[]";
        }

    }

    private ArrayList<String> parseNames() {
        //returns an arraylist with all the student names
        ArrayList<String> names = new ArrayList<String>();
        try {
            JSONArray obj = new JSONArray(json_string);
            for (int i = 0; i < obj.length(); i++) {
                names.add(obj.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
            //send back error message to populate list view
            ArrayList<String> invalid = new ArrayList<String>();
            invalid.add(e.toString());
            return (invalid);
        }
        return (names);

    }

    private ArrayList<LatLng> parseCoords() {
        //returns an arraylist of LatLng objects for all the student's hometowns
        ArrayList<LatLng> coords = new ArrayList<LatLng>();

        try {
            JSONArray array = new JSONArray(json_string);
            for (int i = 0; i < array.length(); i++) {
                double lat = array.getJSONObject(i).getJSONArray("lat_long").getDouble(0);
                double lng = array.getJSONObject(i).getJSONArray("lat_long").getDouble(1);
                LatLng latLng = new LatLng(lat, lng);
                coords.add(latLng);
            }
        } catch (Exception e) {
            //send back whatever coordinates we got
            return (coords);
        }

        return (coords);


    }

    /* BELOW ARE REQUIRED METHODS FOR THE IMPLEMENTED CLASSES */

    @Override
    public void onInfoWindowClick(Marker marker) {
        String id = marker.getId();
        int index = markerTags.get(id);

        //send person index to info activity
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("json", json_string);

        Intent intent = new Intent(getApplicationContext(), PersonInfo.class);
        intent.putExtras(bundle);
        startActivity(intent);

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
        ArrayList<LatLng> coords = parseCoords();
        ArrayList<String> names = parseNames();

        for (int i = 0; i < coords.size(); i++) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(coords.get(i));
            marker.title(names.get(i));

            //keep track of associations between markers and people indexes
            String id = gmap.addMarker(marker).getId();
            markerTags.put(id, i);
        }

        gmap.setOnInfoWindowClickListener(this);

        //set camera initial position to Dartmouth
        LatLng dartmouth = new LatLng(43.7044, -72.2887);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(dartmouth));
    }
}
