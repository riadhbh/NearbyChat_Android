package com.example.kaisriadh.maps3.SQLite;

import android.provider.BaseColumns;

/**
 * Created by riadh on 12/13/2016.
 */

public class DatabaseContract {
    public interface Login extends BaseColumns{
        String tablename = "login";
//        String coluid="uid";
//        String coluidType="TEXT";
        String colmail="mail";
        String colmailType="TEXT";
        String colpass="pass";
        String colpassType="TEXT";
    }
}
