package com.eventus.privileb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;
import com.navdrawer.SimpleSideDrawer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.eventus.privileb.General.Functionalities.isEmailValid;

public class RegisterActivity extends AppCompatActivity {

    private Context context = this;
    private DatePickerDialog birthDatePickerDialog;
    private SimpleSideDrawer mNav;
    private EditText txtSN1, txtSN2, txtSN3, txtSN4, txtFirstName, txtLastName, txtBirthDate, txtAddress, txtMobileNumber, txtEmail, txtPassword, txtConfirmPassword;
    private RadioButton rbMale, rbFemale;
    private CheckBox chkAgree;
    private Spinner spCountry;
    private WSC _wsc;
    SharedPreferences prefs;
    String selectedSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _wsc = new WSC();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        ImageButton btnCalendar = (ImageButton)findViewById(R.id.calender_btn);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
                birthDatePickerDialog.show();
            }
        });

        Button btnRegsiterActivate = (Button) findViewById(R.id.register_activate);
        btnRegsiterActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_user();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Call Hotline");
                    alertDialog.setMessage("81717272");
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

        RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.register_menu);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleLeftDrawer();
            }
        });

        txtSN1 = (EditText) findViewById(R.id.register_sn1);
        txtSN2 = (EditText) findViewById(R.id.register_sn2);
        txtSN3 = (EditText) findViewById(R.id.register_sn3);
        txtSN4 = (EditText) findViewById(R.id.register_sn4);
        txtFirstName = (EditText) findViewById(R.id.register_first_name);
        txtLastName = (EditText) findViewById(R.id.register_last_name);
        txtBirthDate = (EditText) findViewById(R.id.register_birthdate);
        rbMale = (RadioButton) findViewById(R.id.register_radioButton1);
        rbFemale = (RadioButton) findViewById(R.id.register_radioButton2);
        spCountry = (Spinner) findViewById(R.id.register_country);
        txtAddress = (EditText) findViewById(R.id.register_address);
        txtMobileNumber = (EditText) findViewById(R.id.register_mobile_number);
        txtEmail = (EditText) findViewById(R.id.register_email);
        txtPassword = (EditText) findViewById(R.id.register_password);
        txtConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        chkAgree = (CheckBox) findViewById(R.id.register_chk_agree);
        // spCountry.setOnItemSelectedListener(new countryItemSelectedListener());

        _wsc.loadRegister(this, (LinearLayout) findViewById(R.id.register_main));

        RelativeLayout rlMenuRegister = (RelativeLayout) findViewById(R.id.menu_register_title);
        rlMenuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView txtRegister = (TextView) findViewById(R.id.menu_register_txt);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtSN1.requestFocus();
        txtSN1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtSN1.length() == 4){
                    txtSN2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });

        txtSN2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtSN2.length() == 4){
                    txtSN3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });

        txtSN3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtSN3.length() == 4){
                    txtSN4.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });
    }

    void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                txtBirthDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void register_user(){
        if (txtSN1.getText().toString().trim().equals("") || txtSN2.getText().toString().trim().equals("") || txtSN3.getText().toString().trim().equals("") || txtSN4.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("All serial number fields should be filled");
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
        if (txtFirstName.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("First Name should not be empty");
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
        if (txtLastName.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Last Name should not be empty");
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
        if (txtBirthDate.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Birth date should not be empty");
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
        if (rbMale.isChecked() == false && rbFemale.isChecked() == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Gender should be selected");
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

        if (spCountry.getSelectedItem() == null || spCountry.getSelectedItem().toString() == ""){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Country should be selected");
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
        if (txtAddress.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Address should not be empty");
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
        if (txtMobileNumber.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Mobile number should not be empty");
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
        if (txtEmail.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
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
        if (isEmailValid(txtEmail.getText().toString().trim()) == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Email is not valid");
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
            alertDialog.setTitle("Register");
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
        if (txtConfirmPassword.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Confirm password should not be empty");
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
        if (txtPassword.getText().toString().trim().equals(txtConfirmPassword.getText().toString().trim()) == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Password and confirm password should be the same");
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
        if (chkAgree.isChecked() == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage("Please agree to our Terms of Services and Privacy Policy");
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
        String gender = "";
        if (rbMale.isChecked() == true){
            gender = "male";
        }
        if (rbFemale.isChecked() == true){
            gender = "female";
        }
        int selected_country = prefs.getInt(Enums.RegisterCountrySelected, 0);
        _wsc.register(this, txtSN1.getText().toString().trim() + " " + txtSN2.getText().toString().trim() + " " + txtSN3.getText().toString().trim() + " " + txtSN4.getText().toString().trim(), txtFirstName.getText().toString().trim(), txtLastName.getText().toString().trim(), txtBirthDate.getText().toString().trim(), gender, selected_country + "",txtAddress.getText().toString().trim(), txtMobileNumber.getText().toString().trim(),txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());
    }

}
