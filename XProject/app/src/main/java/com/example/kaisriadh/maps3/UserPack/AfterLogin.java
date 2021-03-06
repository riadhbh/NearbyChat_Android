package com.example.kaisriadh.maps3.UserPack;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kaisriadh.maps3.MyBottomSheetDialogFragment;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.SQLite.DataBaseOpenHelper;
import com.example.kaisriadh.maps3.SQLite.DatabaseContract;
import com.example.kaisriadh.maps3.UserPack.CreateAccount;
import com.example.kaisriadh.maps3.UserPack.Login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class AfterLogin extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnGroundOverlayClickListener {
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    //private BottomSheetBehavior bottomSheetBehavior;
    private GroundOverlay mGroundOverlayRotated;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Toolbar toolbar;
    private DataBaseOpenHelper dbhlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mapFragment.getMapAsync(this);
        dbhlp = new DataBaseOpenHelper(getBaseContext());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_after_login, menu);
        return true;
    }

    private void GotoAccountSettings() {
        Intent intent = new Intent(getBaseContext(), Account_Settings.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            GotoAccountSettings();
            return true;

        } else if (id == R.id.action_msg) {
            new MyBottomSheetDialogFragment().show(getSupportFragmentManager(), "sample");
            return true;
        } else if (id == R.id.action_logout) {
            Logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        new MyBottomSheetDialogFragment().show(getSupportFragmentManager(), "sample");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(getApplicationContext(), mLastLocation.toString(), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();
        }
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Location services connection failed with code " + result.getErrorCode(), Toast.LENGTH_LONG).show();

        }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double currentLatitude,currentLongitude;
        if(mLastLocation==null)
        {
            currentLatitude = 0;
            currentLongitude = 0;
        }else {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();
        }
        mMap.setOnGroundOverlayClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(36.766101,10.109642);
        mGroundOverlayRotated=mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.unnamed))
                .position(sydney,10f, 10f).bearing(30).clickable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,20));
    }

    private void Logout(){
        SQLiteDatabase database=dbhlp.getWritableDatabase();
        database.delete(DatabaseContract.Login.tablename,null,null);//delete all rows
        finishAffinity();
    }
}
