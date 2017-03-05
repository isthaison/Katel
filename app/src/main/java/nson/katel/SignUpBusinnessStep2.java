package nson.katel;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUpBusinnessStep2 extends AppCompatActivity
        implements
        OnMapReadyCallback{
    private static final int MY_REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private ActionBar actionBar;
    private EditText edt_duong,edt_tinh,edt_thanhpho,edt_quocgia,edt_ma;
    private Button btn_xacnhan;
    private ProgressDialog myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_businness_step2);
        edt_duong=(EditText)findViewById(R.id.edt_signup2_duong);
        edt_tinh=(EditText)findViewById(R.id.edt_signup2_tinh);
        edt_thanhpho =(EditText)findViewById(R.id.edt_signup2_thanhpho);
        edt_quocgia =(EditText)findViewById(R.id.edt_signup2_quocgia);
        btn_xacnhan=(Button)findViewById(R.id.btn_signup2_xacnhan);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Chọn địa chỉ");

        //Tạo Progress Bar
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Đang tải Map ...");
        myProgress.setMessage("Vui lòng chờ...");
        myProgress.setCancelable(false);
        myProgress.show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_signup_step2);
        mapFragment.getMapAsync(this);
        eventLocationHere();

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
                    if (ContextCompat.checkSelfPermission(SignUpBusinnessStep2.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            InfoHereAddress(location.getLatitude(),location.getLongitude());
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
        if (ContextCompat.checkSelfPermission(SignUpBusinnessStep2.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpBusinnessStep2.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(SignUpBusinnessStep2.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
            }else {
                ActivityCompat.requestPermissions(SignUpBusinnessStep2.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
            }
        }else {
            LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                InfoHereAddress(location.getLatitude(),location.getLongitude());

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Not thing",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public void InfoHereAddress(Double lat, Double lon){
        String city ="";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<android.location.Address> m_address;
        try {
            m_address= geocoder.getFromLocation(lat,lon,1);
            if (m_address.size()>0){
                String duong,thanhpho,tinh,quocgia;
                duong = m_address.get(0).getAddressLine(0);
                thanhpho= m_address.get(0).getLocality();
                tinh =m_address.get(0).getAdminArea();
                quocgia =m_address.get(0).getCountryName();
                edt_duong.setText(duong);
                edt_thanhpho.setText(thanhpho);
                edt_quocgia.setText(quocgia);
                edt_tinh.setText(tinh);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
