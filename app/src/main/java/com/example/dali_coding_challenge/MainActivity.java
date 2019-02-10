package com.example.dali_coding_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void nav_to_people(View view) {
        Intent intent = new Intent(this, People.class);
        startActivity(intent);
    }

    public void nav_to_hometowns(View view) {
        Intent intent = new Intent(this, People.class);
        startActivity(intent);
    }

    public void nav_to_participation(View view) {
        Intent intent = new Intent(this, People.class);
        startActivity(intent);
    }
}
