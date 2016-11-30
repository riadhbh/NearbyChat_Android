package com.example.kaisriadh.maps3;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private LinearLayout linearLayout;
    private LocationManager locationManager;
//    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler prb_handler = new Handler();
    Handler handler = new Handler();
    //private TextView GpsAlert;
    //private ProgressDialog progressDialog;
    private boolean res;
    @Override
    protected void onRestoreInstanceState(Bundle savedlnstanceState){
        super.onRestoreInstanceState(savedlnstanceState);

        try{
            AnimateProgressBar();
            StartUpTester();
/*
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                GpsAlert.setVisibility(View.VISIBLE);
            else{
                GpsAlert.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                nextac();
            }

*/
        }catch(NullPointerException e){

        }

    }
    @Override
    protected void onResume(){
    super.onResume();
        try{
            AnimateProgressBar();
            StartUpTester();
 /*
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            GpsAlert.setVisibility(View.VISIBLE);
            else{
            GpsAlert.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            nextac();
        }
*/

        }catch(NullPointerException e){

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        hideSystemUi();
        linearLayout=(LinearLayout)findViewById(R.id.layout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //GpsAlert=(TextView)findViewById(R.id.GpsAlert);
        //GPSTester();
/*        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            GpsAlert.setVisibility(View.VISIBLE);
        }*/

        /*gpstest:
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();
        break gpstest;
        }*/
/*
        GpsAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
*/
        AnimateProgressBar();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                StartUpTester();

                //Toast.makeText(getBaseContext(), "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
               /*SeverHandshake();
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    GpsAlert.setVisibility(View.VISIBLE);
                else{
                    GpsAlert.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    nextac();
                }*/
            }
        },5000);

        //nextac();
    }





private void AnimateProgressBar(){

    new Thread(new Runnable() {
        @Override
        public void run() {
            while (pStatus <= 100) {
                prb_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(pStatus);
                        //txtProgress.setText(pStatus + " %");
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pStatus++;
            }
        }
    }).start();

}

    private void StartUpTester(){
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            GPSDiag();
        }else
            SeverHandshake();
    }

    public void ServerhandshakeDiag(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.caution);
        builder.setCancelable(false);
        builder.setTitle("Cannot Contact Server");
        builder.setMessage("Please Check your Network Settings !");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                AnimateProgressBar();
                SeverHandshake();
                //finish();
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });

        builder.show();
    }

    public void GPSDiag(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.caution);
        builder.setCancelable(false);
        builder.setTitle("Location service is Diabled");
        builder.setMessage("Please Enable the Location Service !");

        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                //finish();
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });

        builder.show();
    }

    private void SeverHandshake(){
       //res=false;
/*        progressDialog = ProgressDialog.show(MainActivity.this, "",
                "Contatcting server, Please wait...", true);*/
        String url= Server_Host_Constant.Host+"/handshake.php";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("Yes i'm here")){
//                    res=true;
//                    progressDialog.dismiss();

                        ServerhandshakeDiag();


                }else
                    nextac();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                ServerhandshakeDiag();
                //Toast.makeText(getBaseContext(),"Error while establishing connection with server",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("msg","Are you here ?");
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);

        //return  res;
    }

    private void nextac(){
        Intent intent=new Intent(this,GetStartedActivity.class);
        startActivity(intent);
    }


/*    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }*/
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); }
}
