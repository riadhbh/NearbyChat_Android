package com.example.kaisriadh.maps3.ServerConnection;

/**
 * Created by riadh on 8/19/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static boolean fullofspaces(String str) {
        boolean res = false;
        if (str.charAt(0) == ' ') return true;
        if (str.charAt(str.length() - 1) == ' ') return true;
        char prev = str.charAt(0);
        for (int i = 1; i < str.length() && !res; i++) {
            if (prev == ' ' && str.charAt(i) == prev)
                res = true;
            prev = str.charAt(i);
        }
        return res;
    }

    public static String checkFirstname(String fn) {
        String msg = "";
        if (fn.equals("")) {
            msg += "*You must fill the first name field\n";
        } else {
            if (fn.length() < 2)
                msg += "*The first name field is too short\n";
            if (fullofspaces(fn))
                msg += "*The first name field contains too many spaces\n";
        }
        return msg;
    }

    public static String checkLastname(String ln) {
        String msg = "";
        if (ln.length() != 0) {
            if (ln.length() < 2)
                msg += "*The last name field is too short\n";
            if (fullofspaces(ln))
                msg += "*The last name field contains too many spaces\n";
        } else
            msg = "*You must fill the last name field\n";
        return msg;
    }

    public static String checkEmails(String mail, String mailc) {
        String msg = "";
        if (mail.equals("") ||
                mailc.equals(""))
            msg += "*You must fill the email field\n";
        else {
            if (mail.lastIndexOf('@') == -1 ||
                    mailc.lastIndexOf('@') == -1 ||
                    mail.lastIndexOf('.') == -1 ||
                    mailc.lastIndexOf('.') == -1 ||
                    mail.substring(mail.lastIndexOf('.')+1).length() < 2 ||
                    mailc.substring(mailc.lastIndexOf('.')+1).length() < 2 ||
                    mail.substring(mail.lastIndexOf('@')+1,mailc.lastIndexOf('.')).length() < 2 ||
                    mailc.substring(mailc.lastIndexOf('@')+1,mailc.lastIndexOf('.')).length() < 2 ||
                    mail.indexOf('@')!=mail.lastIndexOf('@')||
                    mailc.indexOf('@')!=mailc.lastIndexOf('@')
                    )
                msg += "*The email address must be like mail@domain.com\n";
            if (mailc.lastIndexOf(' ') != -1 || mail.lastIndexOf(' ') != -1)
                msg += "*The email address musn't contain spaces\n";
            if (!mail.equals(mailc))
                msg += "*The emails don't match\n";
        }
        return msg;
    }

    public static String checkEmail(String mail) {
        String msg = "";
        if (mail.lastIndexOf('@') == -1 ||
                mail.lastIndexOf('.') == -1 ||
                mail.substring(mail.lastIndexOf('.')+1).length() < 2 ||
                mail.substring(mail.lastIndexOf('@')+1,mail.lastIndexOf('.')).length() < 2 ||
                mail.indexOf('@')!=mail.lastIndexOf('@')
                )
            msg += "*The email address must be like mail@domain.com\n";
        if (mail.contains(" "))
            msg += "*The email address musn't contain spaces\n";
        return msg;
    }

    public static String checkPasswords(String psw, String cpsw) {
        String msg = "";
        if (psw.equals("") ||
                cpsw.equals(""))
            msg += "*You must fill the password field\n";
        else {
            if (psw.length() < 8 ||
                    cpsw.length() < 8)
                msg += "*Password length must be at least 8 characters\n";

            if (!psw.equals(cpsw))
                msg += "*The passwords don't match\n";
            if (psw.lastIndexOf(' ') != -1 ||
                    cpsw.lastIndexOf(' ') != -1)
                msg += "*The password musn't contain spaces\n";
        }
        return msg;
    }

    public static String checkPassword(String psw) {
        String msg = "";
        if (psw.equals(""))
            msg += "*You must fill the password field\n";
        else {
            if (psw.length() < 8)
                msg += "*Password length must be at least 8 characters\n";

            if (psw.lastIndexOf(' ') != -1)
                msg = "*The password musn't contain spaces\n";
        }
        return msg;
    }

    public static String checkHotelname(String hname){
        String msg="";
        if (hname.equals("")) {
            msg += "*You must fill the hotel name field\n";
        } else {
            if (hname.length() < 2)
                msg += "*The hotel name field is too short\n";
            if (fullofspaces(hname))
                msg += "*The hotel name field contains too many spaces\n";
        }
        return msg;
    }

    public static String checkHotelwebsite(String hwebsite){
        String msg="";
        if (hwebsite.equals(""))
            msg += "*You must fill the website field\n";
        else{
            if(hwebsite.indexOf(".")<2||
               hwebsite.indexOf(".")==hwebsite.lastIndexOf(".")||
               (hwebsite.lastIndexOf(".")-hwebsite.indexOf("."))<3
                    )
                msg+="*Invalid website address !\n";
        }
        return msg;
    }

    public static String checkPhonenumber(String phnum){
        String msg;
        msg="";
          if(phnum.length()<8)
              msg+="*Invalid phone number\n";
        return msg;
    }
}