package com.example.kaisriadh.maps3.UserPack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.*;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.example.kaisriadh.maps3.R;
import com.example.kaisriadh.maps3.ServerConnection.MySingleton;
import com.example.kaisriadh.maps3.ServerConnection.Server_Host_Constant;


import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends Activity {
    EditText fname,lname,email,cmail,pass,cpass,bday;
    Spinner gendersp;
    //DatePicker bdaypick;
    Button signup;
    Calendar cal;
    DateFormat format1;
    String fn,ln,mail,mailc,gender,psw,cpsw,bdaystr;
    ArrayAdapter<String> GenderAdapter;
    ArrayList<String> genders;

    private void loadForminfos(){
    fn=fname.getText().toString().trim();
    ln=lname.getText().toString().trim();
    mail=email.getText().toString().toLowerCase();
    mailc=cmail.getText().toString().toLowerCase();
    gender=gendersp.getSelectedItem().toString();
    psw=pass.getText().toString();
    cpsw=cpass.getText().toString();
    bdaystr=bday.getText().toString();
}

    private String signupcheck(){
        String msg;
        msg="";
        loadForminfos();
        msg+=MySingleton.checkEmails(mail,mailc);
        msg+=MySingleton.checkFirstname(fn);
        msg+=MySingleton.checkLastname(ln);
        msg+=MySingleton.checkPasswords(psw,cpsw);
        msg+=CheckAge(bdaystr);

        if(msg.equals("")) return "ok";
        else
            return msg;
    }

        private  void PrepareGenderSP(){
            genders=new ArrayList<String>();
            genders.add("Male");
            genders.add("Female");
            GenderAdapter=new ArrayAdapter<String>(this, R.layout.gender_item,genders );
            gendersp.setAdapter(GenderAdapter);
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
        gendersp=(Spinner)findViewById(R.id.gendersp);
        bday=(EditText)findViewById(R.id.ca_bday);
        //bdaypick=(DatePicker)findViewById(R.id.birthday);
        pass=(EditText)findViewById(R.id.cpswd);
        cpass=(EditText)findViewById(R.id.ccpswd);
        signup=(Button)findViewById(R.id.createbtn);
                    format1 = new SimpleDateFormat("yyyy-MM-dd");
                    cal = Calendar.getInstance();

                    final DatePickerDialog.OnDateSetListener dp=new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            cal.set(Calendar.YEAR,year);
                            cal.set(Calendar.MONTH,monthOfYear);
                            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                            bday.setText(format1.format(cal.getTime()));
                            bdaystr =format1.format(cal.getTime());

                        }
                    };
                    bday.setInputType(InputType.TYPE_NULL);
                    bday.setText(format1.format(cal.getTime()));


                    bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog DpDiag=create_DP_Diag(dp);
                DpDiag.show();

            }
        });

                    PrepareGenderSP();
                                       //loadForminfos();


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
                        params.put("bday",bdaystr);
                        params.put("gender",gender);

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

private DatePickerDialog create_DP_Diag(DatePickerDialog.OnDateSetListener dp){
    final DatePickerDialog Dpdialog=new DatePickerDialog(this, dp,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    Dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
return  Dpdialog;
}
   private String CheckAge(String userbday){
       String str="";
       String cursday= format1.format(new Date());
       String curyear=cursday.substring(0,4);
       String usryear=userbday.substring(0,4);
       int diff=Integer.parseInt(curyear)-Integer.parseInt(usryear);
//       Toast.makeText(getBaseContext(),String.valueOf(diff),Toast.LENGTH_LONG).show();
       if(diff<18)
           str="Sorry you're not eligible to use this app";
       else
            str="";
   return str;
   }
}