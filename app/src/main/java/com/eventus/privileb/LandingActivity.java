package com.eventus.privileb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;

import com.eventus.privileb.General.Enums;

import java.util.Random;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class LandingActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        final Handler handler = new Handler();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
        Random randObj = new Random();
        int rand = randObj.nextInt(101);

        /*SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Enums.UserId, 0);
        editor.putLong(Enums.CardId, 0);
        editor.putString(Enums.UserFirstName, "");
        editor.putString(Enums.UserLastName, "");
        editor.putBoolean(Enums.UserLoggedIn, false);
        editor.putBoolean(Enums.KeepMeLoggedIn, false);
        editor.putString(Enums.UserGender, "");
        editor.putString(Enums.LoggedinUserType, "");
        editor.putString(Enums.BranchID, "");
        editor.putString(Enums.WelcomeMsg, "");
        editor.commit(); */
        /*
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(LandingActivity.this , SignupActivity.class);
                    startActivity(i);
                }
            }, 3000);
            */

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        if (prefs.getBoolean(Enums.KeepMeLoggedIn, false) == true){
            if (prefs.getString(Enums.LoggedinUserType, "").equals("Cardholder") == true) {
                Intent intent8 = new Intent(this, FeaturedDealsActivity.class);
                startActivity(intent8);
            }
            else if (prefs.getString(Enums.LoggedinUserType, "").equals("Partner") == true){
                Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
            }
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(Enums.UserId, 0);
            editor.putLong(Enums.CardId, 0);
            editor.putString(Enums.UserFirstName, "");
            editor.putString(Enums.UserLastName, "");
            editor.putBoolean(Enums.UserLoggedIn, false);
            editor.putBoolean(Enums.KeepMeLoggedIn, false);
            editor.putString(Enums.LoggedinUserType, "");
            editor.putString(Enums.UserGender, "");
            editor.putString(Enums.BranchID, "");
            editor.putString(Enums.WelcomeMsg, "");
            editor.commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(LandingActivity.this , SignupActivity.class);
                    startActivity(i);
                }
            }, 3000);

         }
        }

    @Override
    public void onResume()
    {
        super.onResume();
        Random randObj = new Random();
        final Handler handler = new Handler();

        /* SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Enums.UserId, 0);
        editor.putLong(Enums.CardId, 0);
        editor.putString(Enums.UserFirstName, "");
        editor.putString(Enums.UserLastName, "");
        editor.putBoolean(Enums.UserLoggedIn, false);
        editor.putBoolean(Enums.KeepMeLoggedIn, false);
        editor.putString(Enums.UserGender, "");
        editor.putString(Enums.LoggedinUserType, "");
        editor.putString(Enums.BranchID, "");
        editor.putString(Enums.WelcomeMsg, "");
        editor.commit(); */

        /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LandingActivity.this , SignupActivity.class);
                startActivity(i);
            }
        }, 3000);
        */

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LandingActivity.this , SignupActivity.class);
                startActivity(i);
            }
        }, 3000);
        */

        if (prefs.getBoolean(Enums.KeepMeLoggedIn, false) == true){
            if (prefs.getString(Enums.LoggedinUserType, "").equals("Cardholder") == true) {
                Intent intent8 = new Intent(this, FeaturedDealsActivity.class);
                startActivity(intent8);
            }
            else if (prefs.getString(Enums.LoggedinUserType, "").equals("Partner") == true){
                Intent intent = new Intent(this, ScannerActivity.class);
                startActivity(intent);
            }
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(Enums.UserId, 0);
            editor.putLong(Enums.CardId, 0);
            editor.putString(Enums.UserFirstName, "");
            editor.putString(Enums.UserLastName, "");
            editor.putBoolean(Enums.UserLoggedIn, false);
            editor.putBoolean(Enums.KeepMeLoggedIn, false);
            editor.putString(Enums.UserGender, "");
            editor.putString(Enums.LoggedinUserType, "");
            editor.putString(Enums.BranchID, "");
            editor.putString(Enums.WelcomeMsg, "");
            editor.commit();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(LandingActivity.this , SignupActivity.class);
                    startActivity(i);
                }
            }, 3000);

        }

    }
    }
