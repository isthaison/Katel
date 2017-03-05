package nson.katel;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Khai báo biến.................................................................................
    private ActionBarDrawerToggle toggle;
    private AdapterIndex adapterIndex;
    //.....
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TabLayout tab_index;
    private ViewPager viewPager_index;
    private View nav_account;
    private CircleImageView profile_index;
    private TextView txt_name_user,txt_name_bus;

    //Khai báo các biến Firebase....................................................................
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private static final int MY_REQUEST_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();



         mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Picasso.with(navigationView.getHeaderView(0).getContext()).load(user.getPhotoUrl()).into(profile_index);
                    txt_name_user.setText(user.getDisplayName());
                    txt_name_bus.setText(user.getEmail());
                    nav_account.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), InfomationUser.class));
                        }
                    });

                } else {
                    Picasso.with(navigationView.getHeaderView(0).getContext()).load("https://d30y9cdsu7xlg0.cloudfront.net/png/17241-200.png").into(profile_index);
                    txt_name_user.setText("Katel");
                    txt_name_bus.setText("Cuộc sống nằm trong tay bạn");
                    navigationView.getMenu().getItem(3).setVisible(false);



                    nav_account.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    });



                }
            }
        };



        //Khởi tạo..................................................................................
        mfindID();//ánh xạ các widget
        //..........................................................................................


        //Yêu cầu hỗ trợ............................................................................
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //..........................................................................................


        //Xử lý Tablayout và ViewPaper.....................................................................
        adapterIndex = new AdapterIndex(getSupportFragmentManager());
        adapterIndex.addFragments(new IndexKara(), "KARA");
        adapterIndex.addFragments(new IndexHotel(), "HOTEL");
        viewPager_index.setAdapter(adapterIndex);
        tab_index.setupWithViewPager(viewPager_index);
        viewPager_index.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(tab_index));
        //..........................................................................................








        //touch đóng mở cho Drawer..................................................................
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //..........................................................................................


        //Khai báo chọn menu cho drawer
        navigationView.setNavigationItemSelectedListener(this);
        //..........................................................................................

        eventLocationHere();


    }//////////////////////KẾT THÚC ONCREATE/////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Khởi tạo Callback.............................................................................
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //..............................................................................................


    //Cài đặt chức năng cho menu drawer.............................................................
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        if (id==R.id.nav_logout){
                FirebaseAuth.getInstance().signOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //..............................................................................................


    //ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss//
    ////// Ánh xạ widget /////////////////////////////////////////////////
    ///////////////////////BEGIN//////////////////////////////////////////
    //ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss//
    public void mfindID() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_index);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tab_index = (TabLayout) findViewById(R.id.tablayout_index);
        viewPager_index = (ViewPager) findViewById(R.id.viewpager_index);
        nav_account = (View) navigationView.getHeaderView(0).findViewById(R.id.nav_acount);
        profile_index = (CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.profile_index_avatar);
        txt_name_user =(TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_main_name_user);
        txt_name_bus =(TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_main_name_bus);

        ////////////////////////////////END///////////////////////////////////
        //ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss//
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_search:
                    startActivity(new Intent(getApplicationContext(),SearchResult.class));
                return true;
            case R.id.main_filter:
                XulyLayoutFilter();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void  XulyLayoutFilter(){
        final ArrayList<String>listNameTinh = new ArrayList<>();
        final ArrayList<String>listNameQuan = new ArrayList<>();
        View layout_filter;
        AlertDialog alertDialog_filter;
        final Spinner spinner_tinh, spinner_quan;
        AlertDialog.Builder dialogBuilder_filter;
        final ArrayAdapter<String> adapter_spinner_tinh,adapter_spinner_quan;
        final ProgressBar progressBar_filter;

        dialogBuilder_filter = new AlertDialog.Builder(this);
        layout_filter = this.getLayoutInflater().inflate(R.layout.layout_filter, null);
        spinner_tinh =(Spinner)layout_filter.findViewById(R.id.spn_filter_tinh);
        spinner_quan =(Spinner)layout_filter.findViewById(R.id.spn_filter_quan);
        progressBar_filter=(ProgressBar)layout_filter.findViewById(R.id.progressBar_fitler);
        dialogBuilder_filter.setView(layout_filter);
        alertDialog_filter = dialogBuilder_filter.create();
        alertDialog_filter.show();


        adapter_spinner_quan = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listNameQuan);
        adapter_spinner_tinh = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,listNameTinh);
        adapter_spinner_tinh.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter_spinner_quan.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        mData.child(Obj_City.name_class).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        Obj_City city = child.getValue(Obj_City.class);
                        listNameTinh.add(city.name_City);
                        spinner_tinh.setAdapter(adapter_spinner_tinh);
                        adapter_spinner_tinh.notifyDataSetChanged();
                    }
                progressBar_filter.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



        spinner_tinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                listNameQuan.clear();
                adapter_spinner_quan.clear();
                    mData.child(Obj_District.name_class).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                progressBar_filter.setVisibility(View.VISIBLE);
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    Obj_District district = child.getValue(Obj_District.class);
                                    if(district.id_City==position){
                                        listNameQuan.add(district.name_District);
                                        spinner_quan.setAdapter(adapter_spinner_quan);
                                        adapter_spinner_quan.notifyDataSetChanged();
                                    }
                                }
                                progressBar_filter.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST_LOCATION:{
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(Main.this,
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
        if (ContextCompat.checkSelfPermission(Main.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(Main.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
            }else {
                ActivityCompat.requestPermissions(Main.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_LOCATION);
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
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
        List<Address> m_address;
        try {
            m_address= geocoder.getFromLocation(lat,lon,1);
            if (m_address.size()>0){
                Toast.makeText(getApplicationContext(),
                                m_address.get(0).getLatitude()+"\n"+
                                m_address.get(0).getLongitude()+"\n"+
                                m_address.get(0).getCountryName()+"\n"+
                                m_address.get(0).getAdminArea()+"\n"+
                                m_address.get(0).getLocality()+"\n"+
                                m_address.get(0).getFeatureName()+"\n"+
                                m_address.get(0).getPremises()+"\n"+
                                m_address.get(0).getSubLocality()+"\n"+
                                m_address.get(0).getSubThoroughfare()+"\n"+
                                m_address.get(0).getUrl()+"\n"+
                                m_address.get(0).getPremises()
                ,Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  city;
    }


















































}
