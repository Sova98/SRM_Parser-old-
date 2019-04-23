package com.example.asus.SRM_Parser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String id_SRM = "-104169151";
    private String necronID = "222401877";
    private String wah30ID = "236412223";
    private String demonsID = "222401920";
    private String tauID = "222401895";
    private String guardID = "222401931";
    private String knightsID = "222401908";
    private String wolfsID = "240229103";
    private String orksID = "222401924";
    private String marinesID = "222402011";
    private String chaosMarinesID = "222401962";
    private String bloodAngelsID = "222401939";
    private String mechsID = "222401887";
    private String greyKnightsID = "222401873";
    private String darkAngelsID = "222401988";
    private String darkEldarID = "222401861";
    private String eldarID = "222401845";
    private String tiranidsID = "222401851";
    private String aksessuarsID = "222401368";

    private String alyansOrderID = "230839402";
    private String alyansChaosID = "222401821";
    private String alyansDeathID = "222401633";
    private String alyansDestrustionID = "230839597";
    private String colorsID = "222401392";
    private String bitsID = "222860223";
    private String literatureID = "222401321";
    int photo_count = 1000; // кол-во фото, для отображения и парсинга

    private String serviceKey = "d3dc215631bcb402c02a5e9c76c2236f91da50b1ae741fafcce70767a94eadcdeb12a8b54d360d92f764a";
    public static ArrayList<Post> tempAlbum = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<String> alb = new ArrayList<String>(); alb.add(necronID); alb.add(wah30ID);
        parsePhotos(alb, photo_count); //загрузка данных из вк

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //код Куока
        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new DataAdapter(this, tempAlbum));
        // код Данила
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fractions) {
            Intent b = new Intent(getApplicationContext(), Fractions.class);
            startActivity(b);

        } else if (id == R.id.nav_save) {

        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_acces) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //поиск
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView search = (SearchView) mSearch.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //сюда адаптер, то, что мы ищем с помощью поиска
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //открытие нового Активити(окно с товаром)
    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        //пока оставлю, Куок
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent i = new Intent(getApplicationContext(), ProductActivity.class);
            i.putExtra("id", position);
            startActivity(i);
        }
    };

    private void initGridArray() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        DataAdapter postsAdapter = new DataAdapter(this, tempAlbum);
        gridView.setAdapter(postsAdapter);
    }

    // HTTP GET request
    private void makeVkRequest(String method, HashMap<String, String> args, Callback func) throws Exception {
        String access_token = serviceKey;
        args.put("access_token", access_token);
        args.put("v", "5.92");
        StringBuilder res_args = new StringBuilder();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            res_args.append(entry.getKey());
            res_args.append("=");
            res_args.append(entry.getValue());
            res_args.append("&");
        }

        String url = "https://api.vk.com/method/" + method + "?" + res_args;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(func);

    }

    private void parsePhotos(List<String> albums, int count)
    {
        for (int i=0; i < albums.size(); i++){
            HashMap<String, String> args = new HashMap<String, String>();
            args.put("album_id", albums.get(i));
            args.put("count", String.valueOf(count));
            getPhotos(args);
        }
    }
    private void getPhotos(HashMap<String, String> args) { //передавать id_альбомов, которые нужно брать в массиве temp_alb будет висеть результат
        try {
            args.put("owner_id", "-104169151");
            //args.put("count", "1000");

            makeVkRequest("photos.get", args, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    e.printStackTrace();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//обработка ошибки
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String my_response = response.body().string();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject ob = (new JSONObject(my_response)).getJSONObject("response");
                                    JSONArray ar = ob.getJSONArray("items");
                                    for (int i = 0; i < ob.getInt("count"); i++) {
                                        JSONObject object = (JSONObject) ar.get(i);
                                        int max = object.getJSONArray("sizes").length();
                                        JSONObject picture_big = object.getJSONArray("sizes").getJSONObject(max - 1); //фоток всгеда 9 берем самую последнюю
                                        JSONObject picture_small = new JSONObject();
                                        for (int j = 0; j < max; j++) {
                                            picture_small = object.getJSONArray("sizes").getJSONObject(j);
                                            if (picture_small.getString("type") == "q")
                                                j = max + 1;
                                        }

                                        Post good = new Post(object.getString("id"), object.getString("text"), picture_big.getString("url"),
                                                picture_small.getString("url"), object.getString("album_id"));
                                        if(object.getString("text").length() > 0){
                                            tempAlbum.add(good);
                                        }else{
                                            getFirstPhotoComment(object.getString("owner_id"), object.getString("id"), good);
                                        }
                                    }
                                    initGridArray();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Получить 1-ый комментарий фотографии
    private void getFirstPhotoComment(String owner_id, String photo_id, final Post post){
        try{
            HashMap<String, String> args = new HashMap<String, String>();
            args.put("owner_id", owner_id);
            args.put("photo_id", photo_id);
            args.put("count", "1");

            makeVkRequest("photos.getComments",  args, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    e.printStackTrace();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//обработка ошибки
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String my_response = response.body().string();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject ob = (new JSONObject(my_response)).getJSONObject("response");
                                    JSONArray ar = ob.getJSONArray("items");
                                    post.setDescription(ar.getJSONObject(0).getString("text"));
                                    tempAlbum.add(post);
                                    initGridArray();
//                                    for (int i = 0; i < ob.getInt("count"); i++) {
//                                        JSONObject object = (JSONObject) ar.get(i);
//                                        object.getString("text")
////                                        int max = object.getJSONArray("sizes").length();
////                                        JSONObject picture_big = object.getJSONArray("sizes").getJSONObject(max - 1); //фоток всгеда 9 берем самую последнюю
////                                        JSONObject picture_small = new JSONObject();
////                                        for (int j = 0; j < max; j++) {
////                                            picture_small = object.getJSONArray("sizes").getJSONObject(j);
////                                            if (picture_small.getString("type") == "q")
////                                                j = max + 1;
////                                        }
////
////                                        Post good = new Post(object.getString("id"), object.getString("text"), picture_big.getString("url"),
////                                                picture_small.getString("url"), object.getString("album_id"));
////                                        tempAlbum.add(good);
//                                    }
//                                    initGridArray();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }catch (Exception e){

        }

    }
}
