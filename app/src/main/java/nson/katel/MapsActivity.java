package nson.katel;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;
import java.util.Locale;
import java.util.jar.*;


public class MapsActivity extends AppCompatActivity
    implements
        OnMapReadyCallback {


    private static final int MY_REQUEST_LOCATION = 1;
    private ActionBar actionBar;
    private GoogleMap mMap;
    private ProgressDialog myProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Vị trí gần đây");

        //Tạo Progress Bar
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Đang tải Map ...");
        myProgress.setMessage("Vui lòng chờ...");
        myProgress.setCancelable(false);
        myProgress.show();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        myProgress.dismiss();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                eventLocationHere();
                return false;
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST_LOCATION:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MapsActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            String s = getNameCityhere(location.getLatitude(),location.getLongitude());
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Not thing",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }
    public void eventLocationHere(){
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
            }else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
            }
        }else {
            LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                String s = getNameCityhere(location.getLatitude(),location.getLongitude());
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Not thing",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public String getNameCityhere(Double lat, Double lon){
        String city ="";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<android.location.Address> m_address;
        try {
            m_address= geocoder.getFromLocation(lat,lon,1);
            if (m_address.size()>0){
                city += m_address.get(0).getLocality();
                city+= m_address.get(0).getCountryName();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  city;
    }
}
