package com.example.randopei_v3;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PointDeVue extends FragmentActivity implements OnMapReadyCallback {

    //---paramètres constantes pour la map ---
    private final static long REFRESH_FASTER = 100;
    private final static long REFRESH = 500;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    //---paramètres constantes pour la photo ---
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    //---variable pour la map ---
    private GoogleMap mMap;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location gLocation;

    //---variable pour la photo ---

    ImageView mImageView;
    Uri image_uri;

    // Shared pref persistance des données
    Button mSaveBtn;
    public static final String SHARED_PREFS = "sharedPrefs";
    double latitude;
    double longitude;
    public static final String PTVUE_LAT = "ptvue_lat";
    public static final String PTVUE_LON = "ptvue_lon";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_de_vue);

        // --- MAP CODE ET LOCALISATION ---
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(fusedLocationProviderClient == null){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    if(locationResult == null){
                        return;
                    }
                    for(Location location : locationResult.getLocations()){
                        updateMark(location);
                        return;
                    }
                }
            };
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(REFRESH);
        locationRequest.setFastestInterval(REFRESH_FASTER);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mSaveBtn = findViewById(R.id.Save);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //double latitude = gLocation.getAltitude();
                //double longitude = gLocation.getLongitude();
                //System.out.println(altitude+" "+" "+longitude);
                saveData();
            }
        });

        // -------------------------------------------


        //--- PHOTO CODE ---
        mImageView = findViewById(R.id.image_view);
        //mCaptureBtn = findViewById(R.id.capture_image_btn);

        mImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        //permission no enable, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });
        //---------------------------------------------
    }

    //---- Mettre un marker sur la map en fonction de notre position ---
    public void updateMark(Location location){

        gLocation = location;

        if(location != null){

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            if(marker != null) {

                marker.remove();


                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                System.out.println("Location : "+latLng);

            }else {

                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            }
        }else{
            System.out.println("Can't update location");
        }
    }

    // ---------------------------------------------

    @Override
    public  void onResume(){
        super.onResume();
        // --- demande la permission pour la localisation ---
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else{
            startLocationUpdates();
        }
        // ------------------------------------------
    }

    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // --- Permission pour la localisation ---
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startLocationUpdates();
                } else {
                    // permission denied
                    updateMark(null);
                }
                return;
            }
            // --------------------------------------


            //--- Permission caméra ---
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
            //---------------------------------------
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // --- Utilisation de la caméra pour les photos ---
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        //called when image was capture from camera

        //récupération et affichage de mon image dans l'image view de mon activité
        if(resultCode == RESULT_OK){
            //set the image captured to our ImageView
            mImageView.setImageURI(image_uri);
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        latitude = gLocation.getLatitude();
        longitude = gLocation.getLongitude();
        editor.putFloat(PTVUE_LAT, (float) latitude);
        editor.putFloat(PTVUE_LON, (float) longitude);
        editor.commit();
        Toast.makeText(this, latitude+" , "+longitude, Toast.LENGTH_SHORT).show();
    }
}
