package com.example.dali_coding_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class People extends AppCompatActivity {

    private String json_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        //get the names to populate the listview
        ArrayList<String> names = getNames();

        ListView people_list = findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
        people_list.setAdapter(adapter);
        people_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView people_list = findViewById(R.id.list);
                String personName = people_list.getItemAtPosition(position).toString();

                //send person name to info activity
                Bundle bundle = new Bundle();
                bundle.putString("name", personName);
                bundle.putString("json", json_string);

                Intent intent = new Intent(getApplicationContext(), PersonInfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private ArrayList<String> getNames() {
        json_string = "";
        //Allow for non-async connection, which is acceptable in this context as the list is useless without data
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
            //send back error message to populate list view
            ArrayList<String> invalid = new ArrayList<String>();
            invalid.add(e.toString());
            return (invalid);
        }

        return (parseNames(json_string));

    }

    private ArrayList<String> parseNames(String json) {
        ArrayList<String> names = new ArrayList<String>();
        try {
            JSONArray obj = new JSONArray(json);
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

}
