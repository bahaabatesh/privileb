package com.eventus.privileb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;
import com.navdrawer.SimpleSideDrawer;

public class LoginActivity extends AppCompatActivity {

    private Context context = this;
    private ImageView menuLogin;
    private SimpleSideDrawer mNav;
    private ScrollView mainViewGroup;
    private RelativeLayout mainViewGroup_1;
    private ImageView dropButton;
    private TextView views[];
    private RelativeLayout mainViewGroup1;
    private TextView txtTitle;
    private ImageView btnCardback;
    private int selectViewIndex = 0;
    LayoutInflater inflater;
    LinearLayout dropdownView;
    private EditText txtUsername, txtPassword;
    private TextView lnkForgotPassword, txtSignup;
    private CheckBox chkKeepMeLoggedIn;
    private WSC _wsc;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _wsc = new WSC();
        prefs = getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        mainViewGroup = (ScrollView) findViewById(R.id.activity_main);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        txtUsername = (EditText) findViewById(R.id.login_username);
        txtPassword = (EditText) findViewById(R.id.login_password);
        chkKeepMeLoggedIn = (CheckBox) findViewById(R.id.chk_keepmeloggedin);
        lnkForgotPassword = (TextView) findViewById(R.id.login_forgotpassword);
        txtSignup = (TextView) findViewById(R.id.login_signup);

        Button btnAppLogin = (Button)findViewById(R.id.btn_login);
        btnAppLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_in();
            }
        });

        lnkForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_password();
            }
        });

        if (mNav == null){
            mNav = new SimpleSideDrawer(this);
            mNav.setLeftBehindContentView(R.layout.activity_menu);
            RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            });

            RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Call Charile Taxi");
                    alertDialog.setMessage("1514");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
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

            // add new
            RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
            btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Call Hotline");
                    alertDialog.setMessage("1514");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
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

            final ImageView btnMenu = (ImageView)findViewById(R.id.login_menu);

            LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
            llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "5");
                    startActivity(intent);
                }
            });

            LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
            llAllDeals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
            llNearbyMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "1");
                    startActivity(intent);
                }
            });

            LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
            llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "2");
                    startActivity(intent);
                }
            });

            LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
            llCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "3");
                    startActivity(intent);
                }
            });

            LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
            llCharities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CharitiesActivity.class);
                    startActivity(intent);
                }
            });

            LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
            llContactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // contact us
                }
            });
        }

        ImageView menuLogin = (ImageView)findViewById(R.id.login_menu);
        menuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleLeftDrawer();
            }
        });

        txtSignup = (TextView) findViewById(R.id.login_signup);
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /* if (prefs.getString(Enums.LastSelectedEmail, "").trim().length() > 0){
            txtUsername.setText(prefs.getString(Enums.LastSelectedEmail, "").trim());
        } */
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < views.length; i++) {
                if (v.getId() == views[i].getId()) {
                    views[i].setTextColor(0xFFD4AC5B);
                    selectViewIndex = i;
                }
                else {
                    views[i].setTextColor(0xFF000000);
                }
            }
            showViewcontents(selectViewIndex);
        }
    };

    void showViewcontents(int index) {

        mainViewGroup1.removeAllViews();

        switch (index) {

            case 0:
                ViewGroup cardView = (ViewGroup) inflater.inflate(R.layout.activity_carddown, null);
                mainViewGroup1.addView(cardView);
                txtTitle.setText("ABOUT PRIVILEB CARD");
                dropButton.setImageResource(R.drawable.down_arrow1);
                dropdownView.setVisibility(View.GONE);
                break;
            case 1:
                Intent intent = new Intent(context, CardSignActivity.class);
                startActivity(intent);

                break;
            case 2:
                ViewGroup benefitsView = (ViewGroup) inflater.inflate(R.layout.activity_benefits, null);
                mainViewGroup1.addView(benefitsView);
                txtTitle.setText("BENEFITS");
                dropdownView.setVisibility(View.GONE);
                dropButton.setImageResource(R.drawable.down_arrow1);
                break;
            case 3:
                ViewGroup termsView = (ViewGroup) inflater.inflate(R.layout.activity_terms, null);
                mainViewGroup1.addView(termsView);
                txtTitle.setText("TERMS & CONDITIONS");
                dropdownView.setVisibility(View.GONE);
                dropButton.setImageResource(R.drawable.down_arrow1);
                break;
            case 4:
                ViewGroup contactView =(ViewGroup) inflater.inflate(R.layout.activity_mrsdown, null);
                mainViewGroup1.addView(contactView);
                txtTitle.setText("CONTACTS US");
                dropdownView.setVisibility(View.GONE);
                dropButton.setImageResource(R.drawable.down_arrow1);
                break;
            default:
                break;
        }
    }

    public void log_in(){
        if (txtUsername.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Login");
            alertDialog.setMessage("Username should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtUsername.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Forgot Password");
            alertDialog.setMessage("Email should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtPassword.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Login");
            alertDialog.setMessage("Password should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        boolean keep_me_logged_in = chkKeepMeLoggedIn.isChecked();
        _wsc.logIn(this, txtUsername.getText().toString().trim(), txtPassword.getText().toString().trim(), keep_me_logged_in);
    }

    public void forgot_password(){
        if (txtUsername.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Forgot Password");
            alertDialog.setMessage("Username should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtUsername.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Forgot Password");
            alertDialog.setMessage("Email should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        _wsc.forgotMyPassword(this, txtUsername.getText().toString().trim());
    }


}
