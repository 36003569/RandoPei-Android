package com.example.randopei_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class option extends AppCompatActivity {

    ToggleButton toggle;
    private boolean toggleOnOff;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TOGGLE1 = "toggle";

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);
        toggle = findViewById(R.id.toggleButton);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //FAIRE UNE PERSISTANCE DE DONNEES
                //LE SON S'ARRETE PAS
                //CREER UN BUNDLE


                if(isChecked){
                    //saveData();
                    mServ.resumeMusic();
                }else{
                    //saveData();
                    mServ.pauseMusic();
                }
            }
        });
        //loadData();
        updateViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(TOGGLE1, toggle.isChecked());
        editor.apply();
        Toast.makeText(this, "Je suis inéluctable!", Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        toggleOnOff = sharedPreferences.getBoolean(TOGGLE1, true);
    }

    public void updateViews(){
        toggle.setChecked(toggleOnOff);
    }
}
