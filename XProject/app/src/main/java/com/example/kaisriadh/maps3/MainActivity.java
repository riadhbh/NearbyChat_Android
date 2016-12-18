package com.example.kaisriadh.maps3;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.kaisriadh.maps3.SQLite.DataBaseOpenHelper;
import com.example.kaisriadh.maps3.SQLite.DatabaseContract;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;
import com.example.kaisriadh.maps3.UserPack.AccountInfos;
import com.example.kaisriadh.maps3.UserPack.Account_Settings;
import com.example.kaisriadh.maps3.UserPack.AfterLogin;
import com.example.kaisriadh.maps3.UserPack.Login;

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
    private DataBaseOpenHelper dbhlp;
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
        dbhlp =new DataBaseOpenHelper(getBaseContext());

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
                //finishAffinity();
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finishAffinity();

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
                //finishAffinity();
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finishAffinity();

            }
        });

        builder.show();
    }

    private void SeverHandshake(){
       //res=false;
/*        progressDialog = ProgressDialog.show(MainActivity.this, "",
                "Contatcting server, Please wait...", true);*/
        String url= Server_Host_Constant.Host+"/Handshake.php";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("Yes i'm here")){
//                    res=true;
//                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
                        ServerhandshakeDiag();


                }else
                    recoverSession();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                //Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                ServerhandshakeDiag();
                Toast.makeText(getBaseContext(),"Error while establishing connection with server",Toast.LENGTH_LONG).show();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
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


    private String getLogin(){
        String cmail="", cpass="";
        SQLiteDatabase database = dbhlp.getReadableDatabase();
        Cursor cursor= database.query(DatabaseContract.Login.tablename, null,null,
                new String[]{},null,null,null);

        if (cursor.moveToFirst()){
            //do{
            cmail = cursor.getString(cursor.getColumnIndex(DatabaseContract.Login.colmail));
            cpass=cursor.getString(cursor.getColumnIndex(DatabaseContract.Login.colpass));
            // do what ever you want here
            //}while(cursor.moveToNext());
        }
        cursor.close();
        return  cmail+"  "+cpass;
    }

//    Intent intent = new Intent(getBaseContext(),Account_Settings.class);
//    startActivity(intent);
    private void recoverSession(){
        String str=getLogin();
        if(str.length()>2){
            final String mail = str.substring(0,str.indexOf("  "));
            final String psw =str.substring(str.indexOf("  ")+2);



                    String url= Server_Host_Constant.Host+"/login.php";
                    StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.contains("Welcome")){
                                AccountInfos.setUserid(response.substring(0,response.indexOf("  ")));
                                AccountInfos.setFullUsername(response.substring(response.indexOf("Welcome")+8));
                                Toast.makeText(getBaseContext(),"Welcome back "+AccountInfos.getFullUsername()+" !", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(),AfterLogin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }else{

                                Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                                nextac();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(),"Error while establishing connection with server", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<>();
                            params.put("login_mail",mail);//POST["login_mail"]=mail;
                            params.put("login_pass",psw);
                            return params;
                        }
                    };

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);




        }else
            nextac();

    }



}
