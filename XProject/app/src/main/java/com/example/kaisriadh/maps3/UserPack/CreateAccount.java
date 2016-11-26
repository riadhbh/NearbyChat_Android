package com.example.kaisriadh.maps3.UserPack;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;


import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends Activity {
    EditText fname,lname,email,cmail,pass,cpass;
    Button signup;

    String fn,ln,mail,mailc,psw,cpsw;

private void loadForminfos(){
    fn=fname.getText().toString().trim();
    ln=lname.getText().toString().trim();
    mail=email.getText().toString().toLowerCase();
    mailc=cmail.getText().toString().toLowerCase();
    psw=pass.getText().toString();
    cpsw=cpass.getText().toString();
}

    private String signupcheck(){
        String msg;
        msg="";
        loadForminfos();
        msg+=MySingleton.checkEmails(mail,mailc);
        msg+=MySingleton.checkFirstname(fn);
        msg+=MySingleton.checkLastname(ln);
        msg+=MySingleton.checkPasswords(psw,cpsw);


        if(msg.equals("")) return "ok";
        else
            return msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            final Handler start = new Handler();

            start.postDelayed(new Runnable() {
                @Override
                public void run() {
        setContentView(R.layout.create_account);
        fname=(EditText)findViewById(R.id.fname);
        lname=(EditText)findViewById(R.id.lname);
        email=(EditText)findViewById(R.id.lmail);
        cmail=(EditText)findViewById(R.id.ccmail);
        pass=(EditText)findViewById(R.id.cpswd);
        cpass=(EditText)findViewById(R.id.ccpswd);
        signup=(Button)findViewById(R.id.createbtn);
        loadForminfos();


        signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg=signupcheck();

            if(!msg.equals("ok")){
                new android.support.v7.app.AlertDialog.Builder(CreateAccount.this)
                        .setTitle("Sorry, we got confused !")
                        .setIcon(R.drawable.caution)
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
            }else{


                String url= Server_Host_Constant.Host+"/register.php";
                StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                        if(response.contains("created")){
                            finish();
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
                        params.put("email",mail);
                        params.put("firstname",fn);
                        params.put("lastname",ln);
                        params.put("password",psw);
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
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