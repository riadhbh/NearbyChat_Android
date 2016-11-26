package com.example.kaisriadh.maps3.UserPack;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.view.View;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;

import java.util.HashMap;
import java.util.Map;

/**
     * Created by riadh on 8/3/2016.
     */

    public class ForgotPassword extends Activity {
    EditText mail,recovcode,passwd,cpasswd;
    TextView havecode;
        LinearLayout recovcodel, changepasswdl;
        Button sendcode, submitcode, changepasswd;
        boolean codevalid;
        String rmail,rcode;

        private void chkcode(){
            rcode = recovcode.getText().toString();
            if(rcode.length()==8){
                String url= Server_Host_Constant.Host+"/chkrecovcode.php";
                StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                        if(response.equals("Code is valid")){
                            changepasswdl.setVisibility(View.VISIBLE);
                            codevalid=true;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Error while reading data", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("recov_code",rcode);
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);


            }else{
                Toast.makeText(ForgotPassword.this,"Code is not valid", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            try{
                final Handler start = new Handler();

                start.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                setContentView(R.layout.forgot_password);
                mail=(EditText)findViewById(R.id.recovmail);
                recovcode=(EditText)findViewById(R.id.recovcode);
                passwd=(EditText)findViewById(R.id.newpaswd);
                cpasswd=(EditText)findViewById(R.id.confpaswd);
                havecode=(TextView)findViewById(R.id.havecode);
                sendcode=(Button)findViewById(R.id.sndrecov);
                submitcode=(Button)findViewById(R.id.submitc);
                changepasswd=(Button)findViewById(R.id.changep);
                recovcodel=(LinearLayout)findViewById(R.id.recovcodel);
                changepasswdl=(LinearLayout)findViewById(R.id.chagepassl);
                rmail="";
                rcode="";
                codevalid=false;
                sendcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mail treatments

                       rmail=mail.getText().toString().toLowerCase();

                        String msg=MySingleton.checkEmail(rmail);
                        if(msg.equals("")){

                            String url=Server_Host_Constant.Host+"/sendrecovcode.php";
                            StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                        Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                                        if(response.contains("success"))
                                        recovcodel.setVisibility(View.VISIBLE);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getBaseContext(),"Error while reading data", Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params=new HashMap<>();
                                    params.put("recov_mail",rmail);
                                    return params;
                                }
                            };

                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);

                        }else{
                                new android.support.v7.app.AlertDialog.Builder(ForgotPassword.this)
                                        .setTitle("Sorry, incorrect Email !")
                                        .setIcon(R.drawable.caution)
                                        .setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).create().show();
                            }
                    }
                });

                havecode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recovcodel.setVisibility(View.VISIBLE);
                    }
                });

                submitcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //recovery code treatment
                            chkcode();
                           }

                });

                changepasswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rcode = recovcode.getText().toString();
                        final String fpass,cpass,msg;
                        fpass=passwd.getText().toString();
                        cpass=cpasswd.getText().toString();
                        msg=MySingleton.checkPasswords(fpass,cpass);

                         if(msg.equals("")){

                             String url=Server_Host_Constant.Host+"/recovpass.php";
                             StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                 @Override
                                 public void onResponse(String response) {

                                     Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                                     finish();
                                 }
                             }, new Response.ErrorListener() {
                                 @Override
                                 public void onErrorResponse(VolleyError error) {
                                     Toast.makeText(getBaseContext(),"Error while reading data", Toast.LENGTH_LONG).show();
                                 }
                             }){
                                 @Override
                                 protected Map<String, String> getParams() throws AuthFailureError {
                                     Map<String,String> params=new HashMap<>();
                                     params.put("recov_code",rcode);
                                     params.put("new_pass",fpass);
                                     return params;
                                 }
                             };

                             MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);


                        }else{

                            new android.support.v7.app.AlertDialog.Builder(ForgotPassword.this)
                                    .setTitle("Sorry, we got confused !")
                                    .setIcon(R.drawable.caution)
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create().show();
                        }
                    }
                });

        }
}, 300);

        }catch(RuntimeException e){
        e.printStackTrace();
        }
        }

    }
