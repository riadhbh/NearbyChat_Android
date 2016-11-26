package com.example.kaisriadh.maps3.UserPack;

import android.content.Context;

/**
 * Created by riadh on 8/19/2016.
 */
public class AccountInfos {
    private static String fullUsername;
    private static String Userid;

    private static Context mCtx;

    AccountInfos(Context ctxt){
        mCtx=ctxt;
    }

    public static String getUserid() {
        return Userid;
    }


    public static void setUserid(String userid) {
        AccountInfos.Userid = userid;
    }

    public static String getFullUsername() {
        return fullUsername;
    }

    public static void setFullUsername(String fullUsername) {
        AccountInfos.fullUsername = fullUsername;
    }
}