package com.example.kaisriadh.maps3.UserPack;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.android.volley.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

import com.android.volley.toolbox.*;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.*;
/**
 * Created by riadh on 8/17/2016.
 */


public class Account_Settings extends Activity {
    LinearLayout pinfosl, chpassl;
    TextView pinfos, chpass, stfullname;
    EditText firstname, lastname, email,confmail,oldpass, newpass, confpass;
    Button save;
    String fn,ln,fmail,mailc;

    private void getinfos_from_DB() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            String url= Server_Host_Constant.Host+"/getinfos.php";
            StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.equals("")){
                        email.setText(response.substring(0, response.indexOf("  ")));
                        confmail.setText(response.substring(0, response.indexOf("  ")));
                        firstname.setText(response.substring(response.indexOf("  ") + 2, response.lastIndexOf("  ")));
                        lastname.setText(response.substring(response.lastIndexOf("  ") + 2));
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
                    params.put("user_id",AccountInfos.getUserid());
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
        }
    }

    private void update_account_infos() throws ExecutionException, InterruptedException {
     String msg;
        msg="";
        fn=firstname.getText().toString().trim();
        ln=lastname.getText().toString().trim();
        fmail=email.getText().toString().toLowerCase();
        mailc=confmail.getText().toString().toLowerCase();

        msg+=MySingleton.checkFirstname(fn);
        msg+=MySingleton.checkLastname(ln);
        msg+=MySingleton.checkEmails(fmail,mailc);

    if(msg.equals("")){
        fn=firstname.getText().toString().trim();
        ln=lastname.getText().toString().trim();
        fmail=email.getText().toString().toLowerCase();
        stfullname.setText(fn+" "+ln);
        AccountInfos.setFullUsername(fn+" "+ln);
        String url=Server_Host_Constant.Host+"/updateinfos.php";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")){
                    pinfosl.setVisibility(View.GONE);
                    chpassl.setVisibility(View.GONE);
                }
                Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
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
                params.put("user_id",AccountInfos.getUserid());
                params.put("user_mail",fmail);
                params.put("user_fn",fn);
                params.put("user_ln",ln);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }else{

        new AlertDialog.Builder(Account_Settings.this)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            final Handler start = new Handler();

            start.postDelayed(new Runnable() {
                @Override
                public void run() {
        setContentView(R.layout.account_settings);
        //AccountInfos ai=new AccountInfos();
        pinfosl = (LinearLayout) findViewById(R.id.aspinfosl);
        chpassl = (LinearLayout) findViewById(R.id.aspassl);
        pinfos = (TextView) findViewById(R.id.stpinfostxt);
        chpass = (TextView) findViewById(R.id.stpasstxt);
        stfullname = (TextView) findViewById(R.id.stfullname);

        firstname = (EditText) findViewById(R.id.stfn);
        lastname = (EditText) findViewById(R.id.stln);
        email = (EditText) findViewById(R.id.stmail);
        confmail= (EditText) findViewById(R.id.stconfmail);
        oldpass = (EditText) findViewById(R.id.stoldpass);
        newpass = (EditText) findViewById(R.id.stnewpass);
        confpass = (EditText) findViewById(R.id.stcnfpass);
        save = (Button) findViewById(R.id.savebtn);
        getinfos_from_DB();
            stfullname.setText(AccountInfos.getFullUsername());


        pinfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getinfos_from_DB();
                pinfosl.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                chpassl.setVisibility(View.GONE);

            }
        });

        chpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chpassl.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                pinfosl.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (pinfosl.getVisibility() == View.VISIBLE) {
                    try {
                        update_account_infos();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if (chpassl.getVisibility() == View.VISIBLE) {
                    final String fpass;
                    final String cpass;
                    final String oldpsw;
                    String msg;
                    msg = "";
                    oldpsw=oldpass.getText().toString();
                    fpass = newpass.getText().toString();
                    cpass = confpass.getText().toString();
                    msg+=MySingleton.checkPassword(oldpsw);
                    msg+=MySingleton.checkPasswords(fpass,cpass);
                    if (msg.equals("")) {

                        String url=Server_Host_Constant.Host+"/changepass.php";
                        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(!response.equals("")){
                                    if(response.contains("success")){
                                        pinfosl.setVisibility(View.GONE);
                                        chpassl.setVisibility(View.GONE);
                                    }
                                    Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
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
                                params.put("user_id",AccountInfos.getUserid());
                                params.put("old_pass",oldpsw);
                                params.put("new_pass",fpass);
                                return params;
                            }
                        };

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);

                    } else {

                        new AlertDialog.Builder(Account_Settings.this)
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
            }
        });
    }
}, 300);

        }catch(RuntimeException e){
        e.printStackTrace();
        }
    }






}
