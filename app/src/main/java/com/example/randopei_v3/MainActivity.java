package com.example.randopei_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Intent add_act;
    Intent option;
    Button search;
    Button add;
    Button setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_act = new Intent(MainActivity.this, addActivity.class);
        search = findViewById(R.id.search);
        add = findViewById(R.id.B_add);
        setting =  findViewById(R.id.setting);
        option = new Intent(MainActivity.this, option.class);

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void goto_Add(View v){
        startActivity(add_act);
    }
    public void goto_setting(View v){startActivity(option);}

}
