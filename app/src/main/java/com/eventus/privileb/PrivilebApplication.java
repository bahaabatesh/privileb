package com.eventus.privileb;

import android.app.Activity;
import android.app.Application;

import com.navdrawer.SimpleSideDrawer;

/**
 * Created by forever on 5/14/17.
 */

public class PrivilebApplication extends Application{
    public static SimpleSideDrawer mNav;

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse SDK stuff goes here
        mNav = new SimpleSideDrawer((Activity) getApplicationContext());
        mNav.setLeftBehindContentView(R.layout.activity_menu);
    }

}
