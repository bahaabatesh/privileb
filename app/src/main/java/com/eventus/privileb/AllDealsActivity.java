package com.eventus.privileb;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;
import com.eventus.privileb.Util.WSCLogin;
import com.navdrawer.SimpleSideDrawer;

public class AllDealsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private WSC _wscgeneral;
    private WSCLogin _wsc;
    private LinearLayout btnFilter;
    private ImageButton btnMenu;
    EditText editText;
    private LinearLayout tabs[];
    private int selectTabIndex = 0;
    private SimpleSideDrawer mNav6;
    private Button btnFeatured;
    private Button btnAlldeals;
    private LinearLayout linFeatured;
    private LinearLayout linAlldeals;
    private View underFeatured;
    private View underAlldeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);
        _wsc = new WSCLogin();
        _wscgeneral = new WSC();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        if (mNav6 != null){
            mNav6 = null;
        }
        if (mNav6 == null){
            mNav6 = new SimpleSideDrawer(this);
            mNav6.setLeftBehindContentView(R.layout.activity_menu_login);

            TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
            txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

            TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
            txtLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // implement logout funcionality
                    _wsc.logout(AllDealsActivity.this);
                }
            });

            RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(AllDealsActivity.this).create();
                    alertDialog.setTitle("Call Charile Taxi");
                    alertDialog.setMessage("1514");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int result = ContextCompat.checkSelfPermission(AllDealsActivity.this, android.Manifest.permission.CALL_PHONE);
                                    if (result == PackageManager.PERMISSION_GRANTED){
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:1514"));
                                        startActivity(intent);
                                    } else {
                                        _wscgeneral.requestPermission(AllDealsActivity.this);
                                        int result_2 = ContextCompat.checkSelfPermission(AllDealsActivity.this, android.Manifest.permission.CALL_PHONE);
                                        if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:1514"));
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });

            //add new
            RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
            btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(AllDealsActivity.this).create();
                    alertDialog.setTitle("Call Hotline");
                    alertDialog.setMessage("81 717272");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int result = ContextCompat.checkSelfPermission(AllDealsActivity.this, android.Manifest.permission.CALL_PHONE);
                                    if (result == PackageManager.PERMISSION_GRANTED){
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:81717272"));
                                        startActivity(intent);
                                    } else {
                                        _wscgeneral.requestPermission(AllDealsActivity.this);
                                        int result_2 = ContextCompat.checkSelfPermission(AllDealsActivity.this, Manifest.permission.CALL_PHONE);
                                        if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:81717272"));
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });


            LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
            llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "5");
                    startActivity(intent);
                }
            });

            LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
            llAllDeals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, FeaturedDealsActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
            llNearbyMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "1");
                    startActivity(intent);
                }
            });

            LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
            llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "2");
                    startActivity(intent);
                }
            });

            LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
            llCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "3");
                    startActivity(intent);
                }
            });

            LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
            llFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, FavoriteActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_login_charities);
            llCharities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AllDealsActivity.this, CharitiesActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
            llContactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // contact us
                    Intent intent = new Intent(AllDealsActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                }
            });
        }
        btnFilter = (LinearLayout) findViewById(R.id.login_btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllDealsActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });


        btnMenu = (ImageButton) findViewById(R.id.deals_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // contact us
                mNav6.toggleLeftDrawer();
            }
        });

        btnFeatured = (Button) findViewById(R.id.home_btn_featured);
        btnFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllDealsActivity.this, FeaturedDealsActivity.class);
                startActivity(intent);
            }
        });

        underFeatured = (View) findViewById(R.id.underline_feature);

        btnAlldeals = (Button) findViewById(R.id.home_btn_alldeals);
        // btnAlldeals.setOnClickListener(homeClickListener);

        underAlldeals = (View) findViewById(R.id.underline_alldeals);

        linFeatured = (LinearLayout) findViewById(R.id.linear_feature);
        // linFeatured.setOnClickListener(homeClickListener);

        linAlldeals = (LinearLayout) findViewById(R.id.linear_alldeals);
        // linAlldeals.setOnClickListener(homeClickListener);

        btnAlldeals.setTextColor(0xFFD4AC5B);
        underAlldeals.setVisibility(View.VISIBLE);
        btnFeatured.setTextColor(0xFFFFFFFF);
        underFeatured.setVisibility(View.GONE);

        tabs = new LinearLayout[] {
                (LinearLayout)findViewById(R.id.deals_tab_privileb),
                (LinearLayout)findViewById(R.id.deals_tab_nearby),
                (LinearLayout)findViewById(R.id.deals_tab_card),
                (LinearLayout)findViewById(R.id.deals_tab_categories),
                (LinearLayout)findViewById(R.id.deals_tab_favorite)
        };

        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setOnClickListener(tabClickListener2);
        }

        // WSCLogin _wsc = new WSCLogin();
        // _wsc.getUserFavoritesFeaturedDeals(this, (ViewGroup) findViewById(R.id.home_deals_main), prefs.getLong(Enums.UserId, 0) + "", "all_deals", "");

    }

    @Override
    public void onResume()
    {
        super.onResume();
        _wsc.getUserFavoritesFeaturedDeals(this, (ViewGroup) findViewById(R.id.home_deals_main), prefs.getLong(Enums.UserId, 0) + "", "all_deals", "");
    }

    View.OnClickListener tabClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabs.length; i++) {
                if (v.getId() == tabs[i].getId()) {
                    // tabs[i].setAlpha(1.0f);
                    selectTabIndex = i;
                }
                else {
                    if (i != 2) {
                        // tabs[i].setAlpha(0.3f);
                    }
                }
            }
            showMenuContents2(selectTabIndex);
        }
    };

    void showMenuContents2(int index){
        switch (index) {
            case 0:
                Intent intent = new Intent(this, FeaturedDealsActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, LoginHomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, LoginHomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, LoginHomeActivity.class);
                intent3.putExtra("SELECTED_INDEX_ID", "3");
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, FavoriteActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

}
