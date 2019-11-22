package com.example.randopei_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class addActivity extends AppCompatActivity implements View.OnClickListener{

    Intent parking_A ;
    Button parking ;
    Intent d_parcours_A;
    Button d_parcours ;
    Intent pt_vue_A;
    Button pt_vue ;
    Button balise ;
    Intent f_parcours_A;
    Button f_parcours;

    Button btnsave;


    //persistance donnée

    double latitude_parking;
    double longitude_parking;
    double latitude_dbtparcours;
    double longitude_dbtparcours;
    double latitude_ptvue;
    double longitude_ptvue;
    double latitude_finparcours;
    double longitude_finparcours;


    public static final String SHARED_PREFS = "sharedPrefs";

    public static final String PARKING_LAT = "parking_lat";
    public static final String PARKING_LON = "parking_lon";

    public static final String DBTPARCOURS_LAT = "debutparcours_lat";
    public static final String DBTPARCOURS_LON = "debutparcours_lon";

    public static final String PTVUE_LAT = "ptvue_lat";
    public static final String PTVUE_LON = "ptvue_lon";

    public static final String FINPARCOURS_LAT = "finparcours_lat";
    public static final String FINPARCOURS_LON= "finparcours_lon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        parking =  findViewById(R.id.Parking);
        d_parcours = findViewById(R.id.d_parcours);
        pt_vue = findViewById(R.id.pt_vue);
        balise = findViewById(R.id.balise);
        f_parcours = findViewById(R.id.f_parcours);
        parking_A = new Intent(addActivity.this, Parking.class);
        d_parcours_A = new Intent(addActivity.this, DebutParcours.class);
        pt_vue_A = new Intent(addActivity.this, PointDeVue.class);
        f_parcours_A = new Intent(addActivity.this, FinParcours.class);

        btnsave = findViewById(R.id.save);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

    }

    public void click_on_parking(View v){
        startActivity(parking_A);
    }
    public void click_on_dparcours(View v){
        startActivity(d_parcours_A);
    }
    public void click_on_pointdevue(View v){
        startActivity(pt_vue_A);
    }
    public void click_on_fparcours(View v){
        startActivity(f_parcours_A);
    }


    public void closekeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        latitude_parking = sharedPreferences.getFloat(PARKING_LAT, (float)0);
        longitude_parking = sharedPreferences.getFloat(PARKING_LON, (float)0);
        Log.v("position_lon_parking", "position_lat_parking: ("+latitude_parking+","+longitude_parking+")" );

        latitude_dbtparcours = sharedPreferences.getFloat(DBTPARCOURS_LAT, (float)0);
        longitude_dbtparcours = sharedPreferences.getFloat(DBTPARCOURS_LON, (float)0);
        Log.v("position_lon_parking", "position_lat_parking: ("+latitude_parking+","+longitude_parking+")" );

        latitude_parking = sharedPreferences.getFloat(PTVUE_LAT, (float)0);
        longitude_parking = sharedPreferences.getFloat(PTVUE_LON, (float)0);
        Log.v("position_lon_parking", "position_lat_parking: ("+latitude_parking+","+longitude_parking+")" );

        latitude_parking = sharedPreferences.getFloat(FINPARCOURS_LAT, (float)0);
        longitude_parking = sharedPreferences.getFloat(FINPARCOURS_LON, (float)0);
        Log.v("position_lon_parking", "position_lat_parking: ("+latitude_parking+","+longitude_parking+")" );
    }
}
