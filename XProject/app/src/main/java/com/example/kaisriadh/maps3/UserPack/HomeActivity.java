package com.example.kaisriadh.maps3.UserPack;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private GroundOverlay groundOverlay;
    Map<String, GroundOverlay> listGroundOverly = new HashMap<String, GroundOverlay>();

    Map<String, Map<String[], GroundOverlay>> listNeighbors = new HashMap<String, Map<String[], GroundOverlay>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, this);


    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public int getIdDrawableUsingName(String name) {
        return this.getResources().getIdentifier(name, "drawable", this.getPackageName());

    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = " location change New Latitude: " + location.getLatitude() + "New Longitude: " + location.getLongitude();
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        updateposition(location);
        groundOverlay.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20), 2000, null);
    }

    @Override
    public void onProviderEnabled(String s) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.loginopt) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.registeropt) {
            Intent intent = new Intent(this, CreateAccount.class);
            startActivity(intent);
        } else if (id == R.id.optabout) {
            new android.support.v7.app.AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Support")
                    .setMessage("For more support you can contact us via this email address \n" +
                            "xproject.team@yandex.com")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateposition(Location location) {
        String url = Server_Host_Constant.Host + "/updateposition.php";
        final Double currentLatitude = location.getLatitude();
        final Double currentLongitude = location.getLongitude();
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error while reading data", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "1");
                params.put("latitude", String.valueOf(currentLatitude));
                params.put("Longitude", String.valueOf(currentLongitude));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    public void updateNeighborsPosition() {
        String url = Server_Host_Constant.Host + "/getNeighbors.php";
        final Double currentLatitude = location.getLatitude();
        final int id = 1;
        final Double currentLongitude = location.getLongitude();

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                for (int i = 0; i < response.split(" ").length; i = i + 4) {
                    listGroundOverly.get(response.split(" ")[i]).setPosition(new LatLng(Double.parseDouble(response.split(" ")[i + 2]),
                            Double.parseDouble(response.split(" ")[i + 3])));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error while reading data", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("latitude", String.valueOf(currentLatitude));
                params.put("Longitude", String.valueOf(currentLongitude));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    public void bringNeighbor(Location location) {
        String url = Server_Host_Constant.Host + "/getNeighbors.php";
        final Double currentLatitude = location.getLatitude();
        final int id = 1;
        final Double currentLongitude = location.getLongitude();

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), "This is it: -" + response.split(" ")[0] + "- " + response.split(" ")[5] + " " + response.split(" ")[6], Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.split(" ").length; i = i + 4) {
                    listGroundOverly.put(response.split(" ")[i], mMap.addGroundOverlay(new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(response.split(" ")[i + 1], "drawable", getApplicationContext().getPackageName())))
                            .position(new LatLng(Double.parseDouble(response.split(" ")[i + 2]),
                                    Double.parseDouble(response.split(" ")[i + 3])), 10f, 10f).bearing(10).clickable(true))
                    );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error while reading data", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("latitude", String.valueOf(currentLatitude));
                params.put("Longitude", String.valueOf(currentLongitude));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());//(-33.86907833333333,151.20901333333333);//
        GroundOverlayOptions groundOverlayOptions=new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.unnamed))
                .position(sydney,10f, 10f).bearing(10).clickable(true);
        groundOverlay= mMap.addGroundOverlay(groundOverlayOptions);
        bringNeighbor(location);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        final Handler h=new Handler();

        h.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        updateNeighborsPosition();
                        h.postDelayed(this,10000);
                    }
                }, 10000);

    }


}
