package com.example.kaisriadh.maps3.UserPack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity implements Runnable {
TextView createacco,forgotpass;
EditText email,pass;
Button loginbtn;
ProgressDialog progressDialog;

  private void login_fn(){
    final String mail,psw;
    String msg="";
    mail=email.getText().toString().toLowerCase();
    psw=pass.getText().toString();



    msg+= MySingleton.checkPassword(psw);

    if(msg.equals("")){
        progressDialog = ProgressDialog.show(Login.this, "",
                "Logging on, Please wait...", true);
        String url= Server_Host_Constant.Host+"/login.php";
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.contains("Welcome")){
                    Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();

                }else{

                 Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    }else{
        new android.support.v7.app.AlertDialog.Builder(Login.this)
                .setTitle("Sorry, there is some errors !")
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
public void run() {
    login_fn();
}
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try{
        final Handler start = new Handler();

        start.postDelayed(new Runnable() {
            @Override
            public void run() {
    setContentView(R.layout.activity_login);
loginbtn =(Button) findViewById(R.id.loginbtn);
createacco = (TextView) findViewById(R.id.create);
forgotpass = (TextView) findViewById(R.id.forgot);
email=(EditText)findViewById(R.id.lmail);
pass=(EditText)findViewById(R.id.lpass);
    loginbtn.setOnClickListener(new View.OnClickListener() {
      @Override
        public void onClick(View v) {
            login_fn();


        }
    });

    createacco.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent Account_creation = new Intent(v.getContext(),CreateAccount.class);
            startActivityForResult(Account_creation,0);
        }
    });

    forgotpass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent forgotpass = new Intent(v.getContext(),ForgotPassword.class);
            startActivityForResult(forgotpass,0);
        }
    });
}
}, 300);

}catch(RuntimeException e){
e.printStackTrace();
}

}

}