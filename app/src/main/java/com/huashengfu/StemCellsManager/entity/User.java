package com.huashengfu.StemCellsManager.entity;

import android.content.Context;

import com.huashengfu.StemCellsManager.db.DbHandler;

import java.io.Serializable;

public class User implements Serializable {

    public static final String Table = "user";

    public static final String Token = "token";
    public static final String Password = "password";
    public static final String Username = "username";
    public static final String StoreName = "storename";

    private String token = "";
    private String password = "";
    private String username = "";
    private String storename = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public static User getUser(Context context, String username){
        DbHandler dbHandler = DbHandler.getInstance(context);
        return dbHandler.getUser(username);
    }
}
