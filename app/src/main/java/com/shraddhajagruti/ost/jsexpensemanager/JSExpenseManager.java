package com.shraddhajagruti.ost.jsexpensemanager;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Z50 on 19/09/2016.
 */
public class JSExpenseManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }

}
