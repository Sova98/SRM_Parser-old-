package com.example.warhammer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        MainActivity.clickedOnItem = false;

        int id = intent.getExtras().getInt("id");
        Post post = MainActivity.tempAlbum.get(id);
        TextView textView = (TextView) findViewById(R.id.test_id);
        ImageView imageView = (ImageView) findViewById(R.id.test_image);
        Picasso.get().load(post.getPhotoThumb()).into(imageView); //загрузить фото из url в нужный эулумент

        textView.setText(post.getDescription());
//        imageView.setImageResource(imageAdapter.mThumbIds[position]);



    }

}
