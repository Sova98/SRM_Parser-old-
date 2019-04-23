package com.example.asus.SRM_Parser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Fractions extends AppCompatActivity {
    public int  rin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fractions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(c);
                fab.setEnabled(false);
            }
        });


        ListView listView = (ListView)findViewById(R.id.fractions_list);
        String[] fractionsArr = getResources().getStringArray(R.array.fractions_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, fractionsArr);

        listView.setAdapter(adapter);



    }
    /*public void  clickFab(View view){
        while(rin < 1){
            rin++;
            Intent c = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(c);

        }
    }*/

}
