package com.example.dali_coding_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Annual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual);
        getSupportActionBar().setTitle("Terms");

    }

    /* The following functions all start a names list activity with only names for the appropriate term */

    public void fall(View view) {
        Bundle bundle = new Bundle();
        bundle.putChar("term", 'F');
        Intent intent = new Intent(this, People.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void winter(View view) {
        Bundle bundle = new Bundle();
        bundle.putChar("term", 'W');
        Intent intent = new Intent(this, People.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void spring(View view) {
        Bundle bundle = new Bundle();
        bundle.putChar("term", 'S');
        Intent intent = new Intent(this, People.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void summer(View view) {
        Bundle bundle = new Bundle();
        bundle.putChar("term", 'X');
        Intent intent = new Intent(this, People.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
