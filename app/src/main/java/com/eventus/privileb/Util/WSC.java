package com.eventus.privileb.Util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eventus.privileb.FeaturedDealsActivity;
import com.eventus.privileb.General.Config;
import com.eventus.privileb.General.Enums;
import com.eventus.privileb.General.Functionalities;
import com.eventus.privileb.HomeActivity;
import com.eventus.privileb.ImagePopupActivity;
import com.eventus.privileb.OfferActivity;
import com.eventus.privileb.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.eventus.privileb.ScannerActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import dmax.dialog.SpotsDialog;

public class WSC {

    LayoutInflater inflater;
    ImageView ivOfferPanel;
    LinearLayout llOfferPanelEnc;
    SharedPreferences sharedpreferences;
    Marker new_marker;
    Button btn_location;
    AlertDialog progressDialogGlobal;

    public void displayFeaturedOffers(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            final Context final_ctx = ctx;
            progressDialog.show();
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "featured_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                String category = jsonOffer.getString("category");
                                                Log.i("featured", featured);

                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelCategory.setText(category);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void displayAllDeals(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "offers_list.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("response", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {

                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    // .execute(featured);

                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });

                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){

        }

    }

    public void displayOfferDetails(Context ctx, final ViewGroup view_group, String offer_id, final GoogleMap mMap) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "offer_details.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("offer_id", offer_id);
            Log.i("Received Offer ID", offer_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("Received response", response + "");
                            try {
                                JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                JSONObject jsonOffer = jsonData.getJSONObject(0);
                                final String retailer_name = jsonOffer.getString("retailer_name");
                                final String offer_id = jsonOffer.getString("offer_id");

                                String offer_description = jsonOffer.getString("offer_description");
                                String offer_name = jsonOffer.getString("offer_name");
                                String issue_date = jsonOffer.getString("issue_date");
                                String expiry_date = jsonOffer.getString("expiry_date");
                                String validity = jsonOffer.getString("validity");
                                String frequency = jsonOffer.getString("frequency");
                                final String featured = jsonOffer.getString("featured_cropped");
                                String retailer_logo = jsonOffer.getString("retailer_logo");
                                final String offer_url = jsonOffer.getString("url");

                                JSONArray jsonBranches = (JSONArray) jsonOffer.get("branches");
                                JSONObject jsonBranch = jsonBranches.getJSONObject(0);
                                final String latitude = jsonBranch.getString("latitude");
                                final String longitude = jsonBranch.getString("longitude");
                                final String branch_mobile_number = jsonBranch.getString("branch_mobile_number");
                                final String branch_phone_number = jsonBranch.getString("branch_phone_number");
                                String location = jsonBranch.getString("branch_location");
                                JSONArray jsonGalleryCropped = (JSONArray) jsonOffer.get("gallery_cropped");
                                JSONArray jsonSubcategoryArray = (JSONArray) jsonOffer.get("sub_category");
                                JSONObject jsonSubcategory = jsonSubcategoryArray.getJSONObject(0);
                                final String category_name = jsonSubcategory.getString("category_name");


                                LatLng _latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                new_marker = mMap.addMarker(new
                                        MarkerOptions().position(_latlng).title(location));
                                final float _zoom = 10;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_latlng, _zoom));

                                LinearLayout llCroppedGallery = (LinearLayout) view_group.findViewById(R.id.offer_gallery_cropped);
                                ViewGroup _viewGallery = (ViewGroup) inflater.inflate(R.layout.activity_gallery_offer_details, null);
                                ImageView _ivGallery = (ImageView) _viewGallery.findViewById(R.id.offer_detail_gallery);
                                final RelativeLayout _rlProgBar = (RelativeLayout) _viewGallery.findViewById(R.id.gallery_prog_bar);
                                try {
                                    // new DownloadImageTask(ivGallery, final_ctx)
                                    //  .execute(g_cropped);
                                    Picasso.get().load(featured).into(_ivGallery, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            _rlProgBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }

                                    });
                                    _ivGallery.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(final_ctx, ImagePopupActivity.class);
                                            intent.putExtra("SELECTED_GALLERY", featured);
                                            final_ctx.startActivity(intent);
                                        }
                                    });
                                    llCroppedGallery.addView(_viewGallery);
                                }catch(Exception ex){}
                                for (int z = 0; z < jsonGalleryCropped.length(); z++) {
                                    ViewGroup viewGallery = (ViewGroup) inflater.inflate(R.layout.activity_gallery_offer_details, null);
                                    ImageView ivGallery = (ImageView) viewGallery.findViewById(R.id.offer_detail_gallery);
                                    final RelativeLayout rlProgBar = (RelativeLayout) viewGallery.findViewById(R.id.gallery_prog_bar);
                                    final String g_cropped = jsonGalleryCropped.getString(z);
                                    try {
                                        // new DownloadImageTask(ivGallery, final_ctx)
                                        //  .execute(g_cropped);
                                        Picasso.get().load(g_cropped).into(ivGallery, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                rlProgBar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                            }
                                        });
                                        ivGallery.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(final_ctx, ImagePopupActivity.class);
                                                intent.putExtra("SELECTED_GALLERY", g_cropped);
                                                final_ctx.startActivity(intent);
                                            }
                                        });
                                        llCroppedGallery.addView(viewGallery);
                                    } catch (Exception ex) {

                                    }
                                }

                                TextView txtOfferName = (TextView) view_group.findViewById(R.id.offer_detail_name);
                                TextView txtOfferCategory = (TextView) view_group.findViewById(R.id.offer_detail_category);
                                ImageView ivLogo = (ImageView) view_group.findViewById(R.id.offer_detail_logo);
                                TextView txtValidity = (TextView) view_group.findViewById(R.id.offer_detail_validity);
                                TextView txtDate = (TextView) view_group.findViewById(R.id.offer_detail_date);
                                TextView txtFrequency = (TextView) view_group.findViewById(R.id.offer_detail_frequency);
                                final TextView txtLocation = (TextView) view_group.findViewById(R.id.offer_detail_location);
                                TextView txtDescription = (TextView) view_group.findViewById(R.id.offer_detail_description);
                                TextView txtRetailerName = (TextView) view_group.findViewById(R.id.offer_detail_retailer_name);
                                final TextView txtOfferGetDirections = (TextView) view_group.findViewById(R.id.offer_get_directions);
                                final RelativeLayout btnCall = (RelativeLayout) view_group.findViewById(R.id.offer_call);
                                final Activity final_activity = (Activity) final_ctx;

                                String branch_selected_num = "";
                                if (branch_mobile_number.equals("") == false){
                                    branch_selected_num = branch_mobile_number;
                                }
                                else if (branch_phone_number.equals("") == false){
                                    branch_selected_num = branch_phone_number;
                                }
                                final String final_branchselectedno = branch_selected_num;
                                btnCall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Call " + retailer_name);
                                        alertDialog.setMessage(final_branchselectedno);
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        int result = ContextCompat.checkSelfPermission(final_ctx, Manifest.permission.CALL_PHONE);
                                                        if (result == PackageManager.PERMISSION_GRANTED) {
                                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                                            intent.setData(Uri.parse("tel:" + final_branchselectedno));
                                                            final_ctx.startActivity(intent);
                                                        } else {
                                                            requestPermission(final_activity);
                                                            int result_2 = ContextCompat.checkSelfPermission(final_ctx, Manifest.permission.CALL_PHONE);
                                                            if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                                intent.setData(Uri.parse("tel:" + final_branchselectedno));
                                                                final_ctx.startActivity(intent);
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

                                txtOfferName.setText(offer_name);
                                try {
                                    // new DownloadImageTask(ivLogo, final_ctx)
                                    //   .execute(retailer_logo);
                                    Picasso.get().load(retailer_logo).into(ivLogo);
                                } catch (Exception exception) {
                                }
                                txtRetailerName.setText(retailer_name);
                                txtOfferCategory.setText(category_name);
                                txtValidity.setText(validity);
                                txtDate.setText("Date: " + issue_date + " - " + expiry_date);
                                txtFrequency.setText("Frequency: " + frequency);
                                txtLocation.setText("Location: " + location);

                                WebView wv = (WebView) view_group.findViewById(R.id.webview);
                                final String mimeType = "text/html";
                                final String encoding = "UTF-8";
                                wv.loadDataWithBaseURL("", offer_description, mimeType, encoding, "");
                                txtOfferGetDirections.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                                        final_ctx.startActivity(intent);
                                    }
                                });

                                final LinearLayout llOfferLocations = (LinearLayout) view_group.findViewById(R.id.offer_locations);
                                final int branchesNm = jsonBranches.length();
                                if (branchesNm > 0) {
                                    Button[] btnFirst = new Button[branchesNm];
                                    Button[] btnSecond = new Button[branchesNm];
                                    ViewGroup[] vCategoryLabel = new ViewGroup[branchesNm];
                                    for (int x = 0; x < jsonBranches.length(); x++) {
                                        ViewGroup viewCategoryLabel = (ViewGroup) inflater.inflate(R.layout.activity_offer_label, null);
                                        final Button btn1 = (Button) viewCategoryLabel.findViewById(R.id.btn1);
                                        JSONObject jBranch = jsonBranches.getJSONObject(x);
                                        final String a_latitude = jBranch.getString("latitude");
                                        final String a_longitude = jBranch.getString("longitude");
                                        final String a_mobile_number = jBranch.getString("branch_mobile_number");
                                        final String a_phone_number = jBranch.getString("branch_phone_number");
                                        final String a_location = jBranch.getString("branch_location");
                                        final LatLng new_latlng = new LatLng(Double.parseDouble(a_latitude), Double.parseDouble(a_longitude));
                                        btn1.setText(a_location);
                                        if (x == 0) {
                                            addBtnLocation(final_ctx, btn1);
                                        }
                                        vCategoryLabel[x] = viewCategoryLabel;
                                        btn1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                txtLocation.setText("Location: " + a_location);
                                                removeMarker();
                                                removeBtnLocation(final_ctx);
                                                addBtnLocation(final_ctx, btn1);
                                                Marker _marker = mMap.addMarker(new
                                                        MarkerOptions().position(new_latlng).title(a_location));
                                                addMarker(_marker);
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new_latlng, _zoom));
                                                txtOfferGetDirections.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                                Uri.parse("http://maps.google.com/maps?daddr=" + a_latitude + "," + a_longitude));
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                String selected_num = "";
                                                if (a_mobile_number.equals("") == false){
                                                    selected_num = a_mobile_number;
                                                }
                                                else if (a_phone_number.equals("") == false){
                                                    selected_num = a_phone_number;
                                                }
                                                final String final_selected_number = selected_num;
                                                btnCall.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                                        alertDialog.setTitle("Call " + retailer_name);
                                                        alertDialog.setMessage(final_selected_number);
                                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        int result = ContextCompat.checkSelfPermission(final_ctx, Manifest.permission.CALL_PHONE);
                                                                        if (result == PackageManager.PERMISSION_GRANTED) {
                                                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                                                            intent.setData(Uri.parse("tel:" + final_selected_number));
                                                                            final_ctx.startActivity(intent);
                                                                        } else {
                                                                            requestPermission(final_activity);
                                                                            int result_2 = ContextCompat.checkSelfPermission(final_ctx, Manifest.permission.CALL_PHONE);
                                                                            if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                                                intent.setData(Uri.parse("tel:" + final_selected_number));
                                                                                final_ctx.startActivity(intent);
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

                                            }
                                        });
                                        llOfferLocations.addView(viewCategoryLabel);
                                    }
                                    TextView txtShare = (TextView) view_group.findViewById(R.id.share_offer_details);
                                    txtShare.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent i = new Intent(Intent.ACTION_SEND);
                                                i.setType("text/plain");
                                                i.putExtra(Intent.EXTRA_SUBJECT, "Privileb");
                                                String sAux = "";//"\nLet me recommend you this application\n\n";
                                                sAux = sAux + offer_url;
                                                i.putExtra(Intent.EXTRA_TEXT, sAux);
                                                final_ctx.startActivity(Intent.createChooser(i, "Share via"));
                                            } catch (Exception e) {
                                                //e.toString();
                                            }
                                        }
                                    });

                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                                Log.i("Offer Details Exception", exception + "");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void removeMarker(){
        new_marker.remove();
    }

    public void addMarker(Marker m){
        new_marker = m;
    }

    public void removeBtnLocation(Context ctx){
        btn_location.setTextColor(ctx.getResources().getColor(R.color.colorDarkBeige));
        btn_location.setBackgroundResource(R.drawable.btn_border_edit);
    }

    public void addBtnLocation(Context ctx, Button btn){
        btn_location = btn;
        btn_location.setTextColor(ctx.getResources().getColor(R.color.colorWhite));
        btn_location.setBackgroundResource(R.drawable.btn_border_edit_filled);
    }

    public void signUp(Context ctx, String name, String email, String mobile_number) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "newsletter.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("name", name);
            hash_map.put("email", email);
            hash_map.put("mobile_number", mobile_number);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("Received Signup", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    Intent intent = new Intent(final_ctx, HomeActivity.class);
                                    final_ctx.startActivity(intent);
                                } else {
                                    final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                    alertDialog.setTitle("Sign up");
                                    alertDialog.setMessage(strMessage);
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
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

        }

    public void loadRegister(Context ctx, final ViewGroup view_group){

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_countries.php";

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(Enums.RegisterCountrySelected, 0);
            editor.commit();

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("this response", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        final ArrayList<Integer> arrInt = new ArrayList<Integer>(n + 1);
                                        final ArrayList<String> arrList = new ArrayList<String>(n + 1);
                                        for (int i = 0; i < n + 1; i++) {
                                            if (i == 0) {
                                                String country_id = "";
                                                String country_name = "";
                                                arrInt.add(i, Integer.parseInt("0"));
                                                arrList.add(i, country_name);
                                            } else {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i - 1);
                                                String country_id = jsonOffer.getString("country_id");
                                                String country_name = jsonOffer.getString("country_name");
                                                arrInt.add(i, Integer.parseInt(country_id));
                                                arrList.add(i, country_name);
                                            }
                                        }
                                        Spinner spCountry = (Spinner) view_group.findViewById(R.id.register_country);
                                        ArrayAdapter countriesAdapter = new ArrayAdapter(final_ctx, android.R.layout.simple_list_item_1, arrList);
                                        spCountry.setAdapter(countriesAdapter);
                                        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                                String selectedItem = parent.getItemAtPosition(pos).toString();
                                                Log.i("Selected Country", selectedItem);
                                                int selected_id = 0;
                                                for (int x = 0; x < arrList.size(); x++) {
                                                    if (selectedItem == arrList.get(x)) {
                                                        selected_id = arrInt.get(x);
                                                        break;
                                                    }
                                                }
                                                Log.i("selected_id", selected_id + "");
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.putInt(Enums.RegisterCountrySelected, selected_id);
                                                editor.commit();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.putInt(Enums.RegisterCountrySelected, 0);
                                                editor.commit();
                                            }
                                        });

                                    }
                                } else {

                                }
                            } catch (Exception ex) {
                                Log.i("Ex Load Register", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void register(Context ctx, String card_serial_number, String first_name, String last_name, String birth_date, String gender, String country, String address, String mobile_number, final String email, final String password) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "register.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("serial_number", card_serial_number);
            hash_map.put("fname", first_name);
            hash_map.put("lname", last_name);
            hash_map.put("birthdate", birth_date);
            hash_map.put("gender", gender);
            hash_map.put("country_id", country);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            hash_map.put("email", email);
            hash_map.put("datetime", formattedDate);
            hash_map.put("mobile_number", mobile_number);
            hash_map.put("password", password);
            hash_map.put("address", address);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("Received Register", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                    alertDialog.setTitle("Register");
                                    alertDialog.setMessage("You have successfully registered to Privileb Card. Please login to the world of deals.");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Intent intent = new Intent(final_ctx, LoginActivity.class);
                                                    //final_ctx.startActivity(intent);
                                                    logIn(final_ctx, email, password, true);
                                                }
                                            });
                                    alertDialog.show();
                                } else {
                                    final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                    alertDialog.setTitle("Register");
                                    alertDialog.setMessage(strMessage);
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
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void logIn(Context ctx, final String email, final String password, final boolean keep_me_signed){

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "login.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("email", email);
            hash_map.put("password", password);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            //hash_map.put("date", formattedDate);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Enums.KeepMeLoggedIn, keep_me_signed);
            editor.commit();

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                String sType = jsonObj.getString("type");
                                Log.i("Typo", sType);
                                if (strStatus.equals("1")) {
                                    if (sType.equals("Cardholder") == true) {
                                        JSONObject jsonUser = jsonObj.getJSONObject("data");
                                        final String user_id = jsonUser.getString("id");
                                        final String card_id = jsonUser.getString("card_id");
                                        final String fname = jsonUser.getString("fname");
                                        final String lname = jsonUser.getString("lname");
                                        final String gender = jsonUser.getString("gender");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putLong(Enums.UserId, Long.parseLong(user_id));
                                        editor.putLong(Enums.CardId, Long.parseLong(card_id));
                                        editor.putString(Enums.UserFirstName, fname);
                                        editor.putString(Enums.UserLastName, lname);
                                        editor.putBoolean(Enums.UserLoggedIn, true);
                                        editor.putString(Enums.UserGender, gender);
                                        editor.putString(Enums.LoggedinUserType, sType);
                                        /* if (keep_me_signed == true) {
                                            editor.putString(Enums.LastSelectedEmail, email);
                                        }
                                        else {
                                            editor.putString(Enums.LastSelectedEmail, "");
                                        } */
                                        editor.commit();
                                        Intent intent = new Intent(final_ctx, FeaturedDealsActivity.class);
                                        final_ctx.startActivity(intent);
                                    }
                                    else if (sType.equals("Partner") == true){
                                        JSONObject jsonArrayUser = jsonObj.getJSONObject("data");
                                        final String branch_id = jsonArrayUser.getString("id");
                                        //final String partner_logo = jsonUser.getString("partner_logo");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.LoggedinUserType, sType);
                                        editor.putString(Enums.BranchID, branch_id);
                                        editor.putString(Enums.WelcomeMsg, strMessage);
                                        /* if (keep_me_signed == true) {
                                            editor.putString(Enums.LastSelectedEmail, email);
                                        }
                                        else {
                                            editor.putString(Enums.LastSelectedEmail, "");
                                        } */
                                        editor.commit();
                                        Intent intent = new Intent(final_ctx, ScannerActivity.class);
                                        final_ctx.startActivity(intent);

                                    }
                                    else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Log in");
                                        alertDialog.setMessage(strMessage);
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
                                    }
                                } else {
                                    final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                    alertDialog.setTitle("Log in");
                                    alertDialog.setMessage(strMessage);
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
                                }
                            } catch (Exception ex) {
                                Log.i("Login", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void loadScanner(Context ctx, final ViewGroup view_group){

        try {
            //final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            //progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_offers_by_branch_id.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("branch_id", sharedpreferences.getString(Enums.BranchID, ""));

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            hash_map.put("date", formattedDate);

            TextView txtScannerWelcome = (TextView) view_group.findViewById(R.id.scanner_welcome);
            txtScannerWelcome.setText("Welcome " + sharedpreferences.getString(Enums.WelcomeMsg, ""));

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                String sType = jsonObj.getString("type");
                                if (strStatus.equals("1")) {
                                    Log.i("lullaby", response + "");
                                        JSONArray jsonArrayUser = jsonObj.getJSONArray("data");
                                        JSONObject jsonUser = (JSONObject) jsonArrayUser.getJSONObject(0);
                                        final String partner_logo = jsonUser.getString("partner_logo");
                                        ImageView ivScannerImage = (ImageView) view_group.findViewById(R.id.scanner_image);
                                        try {
                                            Picasso.get().load(partner_logo).into(ivScannerImage, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError(Exception e) {

                                                }
                                            });
                                        } catch (Exception exception) {
                                        }
                                        // final String partner_name = jsonUser.getString("partner_name");
                                        JSONArray jsonData = (JSONArray) jsonUser.get("offers");
                                        int n = jsonData.length();
                                        if (n > 0) {
                                            final ArrayList<Integer> arrInt = new ArrayList<Integer>(n + 1);
                                            final ArrayList<String> arrList = new ArrayList<String>(n + 1);
                                            for (int i = 0; i < n + 1; i++) {
                                                if (i == 0) {
                                                    String branchid = "";
                                                    String branchname = "";
                                                    arrInt.add(i, Integer.parseInt("0"));
                                                    arrList.add(i, branchname);
                                                } else {
                                                    JSONObject jsonOffer = jsonData.getJSONObject(i - 1);
                                                    String branchid = jsonOffer.getString("id");
                                                    String branchname = jsonOffer.getString("name");
                                                    arrInt.add(i, Integer.parseInt(branchid));
                                                    arrList.add(i, branchname);
                                                }
                                            }
                                            Spinner spCountry = (Spinner) view_group.findViewById(R.id.scanner_offers);
                                            ArrayAdapter countriesAdapter = new ArrayAdapter(final_ctx, android.R.layout.simple_list_item_1, arrList);
                                            spCountry.setAdapter(countriesAdapter);
                                            spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                                    String selectedItem = parent.getItemAtPosition(pos).toString();
                                                    Log.i("Selected Country", selectedItem);
                                                    int selected_id = 0;
                                                    for (int x = 0; x < arrList.size(); x++) {
                                                        if (selectedItem == arrList.get(x)) {
                                                            selected_id = arrInt.get(x);
                                                            break;
                                                        }
                                                    }
                                                    Log.i("selected_id", selected_id + "");
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putInt(Enums.ScannerOfferSelected, selected_id);
                                                    editor.commit();
                                                    getOfferDescription(final_ctx, (EditText) view_group.findViewById(R.id.scanner_offer_description));
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> arg0) {
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putInt(Enums.ScannerOfferSelected, 0);
                                                    editor.commit();
                                                    // getOfferDescription(final_ctx, (EditText) view_group.findViewById(R.id.scanner_offer_description));
                                                    EditText edtText = (EditText) view_group.findViewById(R.id.scanner_offer_description);
                                                    edtText.setText("");
                                                }
                                            });
                                            // spCountry.setSelection(sharedpreferences.getInt(Enums.ScannerOfferSelected, 0));
                                            Button btnScannerGo = (Button) view_group.findViewById(R.id.scanner_scanoffer);
                                            btnScannerGo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    ScanCard(final_ctx, view_group);
                                                }
                                            });
                                            EditText et1, et2, et3, et4;
                                            et1 = (EditText) view_group.findViewById(R.id.scanner_code1);
                                            et2 = (EditText) view_group.findViewById(R.id.scanner_code2);
                                            et3 = (EditText) view_group.findViewById(R.id.scanner_code3);
                                            et4 = (EditText) view_group.findViewById(R.id.scanner_code4);
                                            if(sharedpreferences.getString(Enums.ScannerSerialNumber, "") != "" && sharedpreferences.getString(Enums.ScannerSerialNumber, "").length() == 4){
                                                et1.setText(sharedpreferences.getString(Enums.ScannerSerialNumber, "").charAt(0));
                                                et2.setText(sharedpreferences.getString(Enums.ScannerSerialNumber, "").charAt(1));
                                                et3.setText(sharedpreferences.getString(Enums.ScannerSerialNumber, "").charAt(2));
                                                et4.setText(sharedpreferences.getString(Enums.ScannerSerialNumber, "").charAt(3));
                                            }
                                    }
                                } else {

                                }
                                // progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.i("scanner error", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}
    }

    public void ScanCard(final Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            String final_snumber = "";
            boolean _proceed = false;

            final EditText et1 = (EditText) view_group.findViewById(R.id.scanner_code1);
            final EditText et2 = (EditText) view_group.findViewById(R.id.scanner_code2);
            final EditText et3 = (EditText) view_group.findViewById(R.id.scanner_code3);
            final EditText et4 = (EditText) view_group.findViewById(R.id.scanner_code4);
            final EditText et = (EditText) view_group.findViewById(R.id.scanner_offer_description);

            String snumber = sharedpreferences.getString(Enums.ScannerSerialNumber, "");
            if (snumber.equals("") == false){
                final_snumber = snumber;
                _proceed = true;
                Log.i("final_snumber", final_snumber);
            }
            else if (et1.getText().toString().trim().equals("") == false && et2.getText().toString().trim().equals("") == false && et3.getText().toString().trim().equals("") == false && et4.getText().toString().trim().equals("") == false){
                _proceed = true;
                final_snumber = et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim() + et4.getText().toString().trim();
                Log.i("final_snumber", final_snumber);
            }
            else {
                _proceed = false;
                final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("Scan Offer");
                alertDialog.setMessage("Please scan or enter the last four digits of your serial number.");
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
                progressDialog.dismiss();
                return;
            }
            if (sharedpreferences.getInt(Enums.ScannerOfferSelected, 0) == 0){
                final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("Scan Offer");
                alertDialog.setMessage("Please select an offer");
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
                progressDialog.dismiss();
                return;
            }
            if (_proceed == true && final_snumber.length() == 4) {
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "scan_offer.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();
                hash_map.put("user_id", "");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());
                hash_map.put("scan_date", formattedDate);
                hash_map.put("offer_id", sharedpreferences.getInt(Enums.ScannerOfferSelected, 0) + "");
                hash_map.put("branch_id", sharedpreferences.getString(Enums.BranchID, ""));
                hash_map.put("serial_number", final_snumber);
                Log.i("scancard", formattedDate + " " + sharedpreferences.getInt(Enums.ScannerOfferSelected, 0) + " " + sharedpreferences.getString(Enums.BranchID, "") + " " + final_snumber);

                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    Log.i("scancard", response + "");
                                    String strStatus = jsonObj.getString("status_code");
                                    String strMessage = jsonObj.getString("message");
                                    if (strStatus.equals("1")) {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                                        alertDialog.setTitle("Scan Offer");
                                        alertDialog.setMessage(strMessage);
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
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.ScannerSerialNumber, "");
                                        editor.putInt(Enums.ScannerOfferSelected, 0);
                                        editor.commit();
                                        Spinner spCountry = (Spinner) view_group.findViewById(R.id.scanner_offers);
                                        spCountry.setSelection(0);
                                        et1.setText("");
                                        et2.setText("");
                                        et3.setText("");
                                        et4.setText("");
                                        et.setText("");

                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                                        alertDialog.setTitle("Scan Offer");
                                        alertDialog.setMessage(strMessage);
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
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.ScannerSerialNumber, "");
                                        editor.putInt(Enums.ScannerOfferSelected, 0);
                                        editor.commit();
                                        Spinner spCountry = (Spinner) view_group.findViewById(R.id.scanner_offers);
                                        spCountry.setSelection(0);
                                        et1.setText("");
                                        et2.setText("");
                                        et3.setText("");
                                        et4.setText("");
                                        et.setText("");
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);
            }
            else {

            }

        }
        catch (Exception ex){}

    }

    public void getOfferDescription(final Context ctx, final EditText offer_desc){

        try {
            // final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            // progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_offer_description.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("offer_id", sharedpreferences.getInt(Enums.ScannerOfferSelected, 0) + "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonOffer = jsonObj.getJSONArray("data");
                                    JSONObject jsonOfferObject = jsonOffer.getJSONObject(0);
                                    String offer_description = jsonOfferObject.getString("offer_description");
                                    offer_desc.setText(Html.fromHtml(offer_description));
                                    // progressDialog.dismiss();
                                }
                                else {
                                    offer_desc.setText("");
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void startBuyingCard(Context ctx, String first_name, String last_name, String email, String mobile_number, String address, String comments, String payment_method_id) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            final String url = Config.wsc_url + "buy_card.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("fname", first_name);
            hash_map.put("lname", last_name);
            hash_map.put("email", email);
            hash_map.put("mobile_number", mobile_number);
            hash_map.put("address", address);
            hash_map.put("comments", comments);
            hash_map.put("payment_method_id", payment_method_id);
            if (payment_method_id.equals("1") == true) {
                hash_map.put("order_status_id", "1");
            } else if (payment_method_id.equals("2") == true) {
                hash_map.put("order_status_id", "2");
            }
            hash_map.put("datetime", formattedDate);

            if (payment_method_id.equals("1") == true) {
                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    String strMessage = jsonObj.getString("message");
                                    if (strStatus.equals("1")) {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Buy Card");
                                        alertDialog.setMessage("Your order was successfully submitted. Our sales department will contact you soon.");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(final_ctx, HomeActivity.class);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });
                                        alertDialog.show();
                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Buy Card");
                                        alertDialog.setMessage(strMessage);
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
                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);
            } else if (payment_method_id.equals("2") == true) {
                /* SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Enums.CardFirstName, first_name);
                editor.putString(Enums.CardLastName, last_name);
                editor.putString(Enums.CardEmail, email);
                editor.putString(Enums.CardMobileNumber, mobile_number);
                editor.putString(Enums.CardAddress, address);
                editor.putString(Enums.CardComments, comments);
                editor.putString(Enums.CardPaymentMethodID, payment_method_id);
                editor.putString(Enums.CardOrderStatusID, "2");
                editor.commit();
                Intent intent = new Intent(final_ctx, CCPaymentActivity.class);
                final_ctx.startActivity(intent); */
                final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("Buy Card");
                alertDialog.setMessage("Coming Soon");
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
            }
        }
        catch (Exception ex){}

    }

        public void forgotMyPassword(Context ctx, String email){

            try {
                final Context final_ctx = ctx;
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "forgot_password.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();
                hash_map.put("email", email);

                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    String strMessage = jsonObj.getString("message");
                                    if (strStatus.equals("1")) {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Forgot Password");
                                        alertDialog.setMessage(strMessage);
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
                                    }
                                } catch (Exception ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);
            }
            catch (Exception ex){}

    }

    public void loadCategories(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_categories.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    LinearLayout llCategoriesLabels = (LinearLayout) view_group.findViewById(R.id.categories_labels);
                                    LinearLayout llCategoriesViews = (LinearLayout) view_group.findViewById(R.id.categories_views);
                                    final ImageView ivCategoriesImage = (ImageView) view_group.findViewById(R.id.categories_image);
                                    ivCategoriesImage.setImageResource(R.drawable.activites);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.CategoriesSelectedForSearch, "");
                                        List<String> newList = new ArrayList<String>();
                                        if (str.equals("") == false) {
                                            str = str.substring(1, str.length() - 1);
                                            str.replace(" ", "");
                                            String[] str_arr = str.split(",");
                                            for (int s = 0; s < str_arr.length; s++) {
                                                if (str_arr[s].trim().equals("") == false) {
                                                    newList.add(str_arr[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrSelectedCategoriesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String category_id = jsonOffer.getString("category_id");
                                            String name = jsonOffer.getString("name");
                                            final String icon_inactive = jsonOffer.getString("icon_inactive");
                                            final String icon_active = jsonOffer.getString("icon_active");
                                            final String img = jsonOffer.getString("image");
                                            ViewGroup viewCategoryLabel = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                            ViewGroup viewCategoryView = (ViewGroup) inflater.inflate(R.layout.activity_category_view, null);
                                            final LinearLayout ll11 = (LinearLayout) viewCategoryLabel.findViewById(R.id.ll11);
                                            final Button btn1 = (Button) viewCategoryLabel.findViewById(R.id.btn1);
                                            LinearLayout ll1 = (LinearLayout) viewCategoryView.findViewById(R.id.ll1);
                                            final ImageView iv1 = (ImageView) viewCategoryView.findViewById(R.id.iv1);
                                            TextView txt1 = (TextView) viewCategoryView.findViewById(R.id.txt1);
                                            final TextView txtStatus = (TextView) viewCategoryView.findViewById(R.id.txtStatus);
                                            btn1.setText(name);

                                            txt1.setText(name);
                                            final int x = i;

                                            ll1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (txtStatus.getText().equals("no")) {
                                                        new DownloadImageTask(iv1, final_ctx)
                                                                .execute(icon_active);
                                                        ivCategoriesImage.setImageBitmap(null);
                                                        new DownloadImageTask(ivCategoriesImage, final_ctx)
                                                               .execute(img);
                                                        txtStatus.setText("yes");
                                                        if (arrSelectedCategoriesList.contains(category_id) == false) {
                                                            arrSelectedCategoriesList.add(category_id);
                                                        }
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(Enums.CategoriesSelectedForSearch, arrSelectedCategoriesList.toString());
                                                        editor.commit();
                                                        loadCategoriesSub(final_ctx, view_group);

                                                    } else if (txtStatus.getText().equals("yes")) {
                                                        ll11.setVisibility(View.INVISIBLE);
                                                        btn1.setVisibility(View.INVISIBLE);
                                                        new DownloadImageTask(iv1, final_ctx).execute(icon_inactive);
                                                        ivCategoriesImage.setImageBitmap(null);
                                                        new DownloadImageTask(ivCategoriesImage, final_ctx)
                                                             .execute(img);
                                                        //iv1.setImageDrawable(final_ctx.getResources().getDrawable(R.drawable.activities));
                                                        txtStatus.setText("no");
                                                        if (arrSelectedCategoriesList.contains(category_id) == true) {
                                                            arrSelectedCategoriesList.remove(category_id);
                                                        }
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(Enums.CategoriesSelectedForSearch, arrSelectedCategoriesList.toString());
                                                        editor.commit();
                                                        loadCategoriesSub(final_ctx, view_group);
                                                    }
                                                }
                                            });

                                            if (arrSelectedCategoriesList.contains(category_id) == true) {
                                                new DownloadImageTask(iv1, final_ctx)
                                                        .execute(icon_active);
                                                txtStatus.setText("yes");
                                                ll11.setVisibility(View.VISIBLE);
                                                llCategoriesLabels.addView(viewCategoryLabel);
                                            } else {
                                                new DownloadImageTask(iv1, final_ctx)
                                                        .execute(icon_inactive);
                                                txtStatus.setText("no");
                                                // ll11.setVisibility(View.GONE);
                                            }

                                            llCategoriesViews.addView(viewCategoryView);
                                        }
                                    }
                                    ViewGroup vCategoryLabel1 = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                    final Button vbtn1 = (Button) vCategoryLabel1.findViewById(R.id.btn1);
                                    vbtn1.setVisibility(View.INVISIBLE);
                                    llCategoriesLabels.addView(vCategoryLabel1);
                                    ViewGroup vCategoryLabel2 = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                    final Button vbtn2 = (Button) vCategoryLabel2.findViewById(R.id.btn1);
                                    vbtn2.setVisibility(View.INVISIBLE);
                                    llCategoriesLabels.addView(vCategoryLabel2);
                                }
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Categories", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void loadCategoriesSub(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_categories.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    LinearLayout llCategoriesLabels = (LinearLayout) view_group.findViewById(R.id.categories_labels);
                                    LinearLayout llCategoriesViews = (LinearLayout) view_group.findViewById(R.id.categories_views);
                                    final ImageView ivCategoriesImage = (ImageView) view_group.findViewById(R.id.categories_image);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    llCategoriesLabels.removeAllViews();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.CategoriesSelectedForSearch, "");
                                        List<String> newList = new ArrayList<String>();
                                        if (str.equals("") == false) {
                                            str = str.substring(1, str.length() - 1);
                                            str.replace(" ", "");
                                            String[] str_arr = str.split(",");
                                            for (int s = 0; s < str_arr.length; s++) {
                                                if (str_arr[s].trim().equals("") == false) {
                                                    newList.add(str_arr[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrSelectedCategoriesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String category_id = jsonOffer.getString("category_id");
                                            String name = jsonOffer.getString("name");
                                            final String icon_inactive = jsonOffer.getString("icon_inactive");
                                            final String icon_active = jsonOffer.getString("icon_active");
                                            final String img = jsonOffer.getString("image");
                                            ViewGroup viewCategoryLabel = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                            ViewGroup viewCategoryView = (ViewGroup) inflater.inflate(R.layout.activity_category_view, null);
                                            final LinearLayout ll11 = (LinearLayout) viewCategoryLabel.findViewById(R.id.ll11);
                                            final Button btn1 = (Button) viewCategoryLabel.findViewById(R.id.btn1);
                                            btn1.setText(name);

                                            if (arrSelectedCategoriesList.contains(category_id) == true) {
                                                llCategoriesLabels.addView(viewCategoryLabel);
                                            } else {
                                            }
                                        }
                                    }
                                    ViewGroup vCategoryLabel1 = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                    final Button vbtn1 = (Button) vCategoryLabel1.findViewById(R.id.btn1);
                                    vbtn1.setVisibility(View.INVISIBLE);
                                    llCategoriesLabels.addView(vCategoryLabel1);
                                    ViewGroup vCategoryLabel2 = (ViewGroup) inflater.inflate(R.layout.activity_category_label, null);
                                    final Button vbtn2 = (Button) vCategoryLabel2.findViewById(R.id.btn1);
                                    vbtn2.setVisibility(View.INVISIBLE);
                                    llCategoriesLabels.addView(vCategoryLabel2);
                                }
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Categories", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

        public void loadFilterByLocation(Context ctx, final ViewGroup view_group) {

            try {
                final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
                progressDialog.show();
                final Context final_ctx = ctx;
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "get_districts.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();
                hash_map.put("country_code", Config.COUNTRY_CODE);

                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    String strMessage = jsonObj.getString("message");
                                    if (strStatus.equals("1")) {
                                        LinearLayout llFilterByLocationChosen = (LinearLayout) view_group.findViewById(R.id.filter__by_location_chosen);
                                        LinearLayout llFilterByLocationOptions = (LinearLayout) view_group.findViewById(R.id.filter_by_location_options);
                                        JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                        int n = jsonData.length();
                                        if (n > 0) {
                                            String str = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
                                            List<String> newList = new ArrayList<String>();
                                            if (str.equals("") == false) {
                                                str = str.substring(1, str.length() - 1);
                                                str.replace(" ", "");
                                                String[] str_arr = str.split(",");
                                                for (int s = 0; s < str_arr.length; s++) {
                                                    if (str_arr[s].trim().equals("") == false) {
                                                        newList.add(str_arr[s].trim());
                                                    }
                                                }
                                            }
                                            final List<String> arrSelectedLocationList = newList;
                                            for (int i = 0; i < n; ++i) {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String district_id = jsonOffer.getString("district_id");
                                                String name = jsonOffer.getString("name");
                                                ViewGroup viewFilterByLocationChosen = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_chosen, null);
                                                ViewGroup viewFilterByLocationOption = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_option, null);
                                                final RelativeLayout rlChosen1 = (RelativeLayout) viewFilterByLocationChosen.findViewById(R.id.rlChosen1);
                                                final TextView txtChosen1 = (TextView) viewFilterByLocationChosen.findViewById(R.id.txtChosen1);
                                                final View vChosen1 = (View) viewFilterByLocationChosen.findViewById(R.id.vChosen1);
                                                final RelativeLayout rlOption1 = (RelativeLayout) viewFilterByLocationOption.findViewById(R.id.rlOption1);
                                                final View vOption1 = (View) viewFilterByLocationOption.findViewById(R.id.vOption1);
                                                final TextView txtOption1 = (TextView) viewFilterByLocationOption.findViewById(R.id.txtOption1);
                                                txtChosen1.setText(name);
                                                txtOption1.setText(name);
                                                rlChosen1.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     rlChosen1.setVisibility(View.GONE);
                                                                                     vChosen1.setVisibility(View.GONE);
                                                                                     rlOption1.setVisibility(View.VISIBLE);
                                                                                     vOption1.setVisibility(View.VISIBLE);
                                                                                     arrSelectedLocationList.remove(district_id);
                                                                                     SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                     editor.putString(Enums.FilterLocationsSelected, arrSelectedLocationList.toString());
                                                                                     editor.commit();
                                                                                 }
                                                                             }
                                                );
                                                rlOption1.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View v) {
                                                                                     rlOption1.setVisibility(View.GONE);
                                                                                     vOption1.setVisibility(View.GONE);
                                                                                     rlChosen1.setVisibility(View.VISIBLE);
                                                                                     vChosen1.setVisibility(View.VISIBLE);
                                                                                     arrSelectedLocationList.add(district_id);
                                                                                     SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                     editor.putString(Enums.FilterLocationsSelected, arrSelectedLocationList.toString());
                                                                                     editor.commit();
                                                                                 }
                                                                             }
                                                );


                                                if (arrSelectedLocationList.contains(district_id) == true) {
                                                    rlOption1.setVisibility(View.GONE);
                                                    vOption1.setVisibility(View.GONE);
                                                    rlChosen1.setVisibility(View.VISIBLE);
                                                    vChosen1.setVisibility(View.VISIBLE);
                                                    Log.i("Sel 2", district_id);
                                                }

                                                llFilterByLocationChosen.addView(viewFilterByLocationChosen);
                                                llFilterByLocationOptions.addView(viewFilterByLocationOption);
                                            }
                                        }
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception ex) {
                                    Log.i("Load Filter Location", ex + "");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);

            }
            catch (Exception ex){}

        }

    public void getSearchInFilterByLocationResults(Context ctx, final ViewGroup view_group, String keyword) {

        try {
            progressDialogGlobal = new SpotsDialog(ctx, R.style.Custom);
            progressDialogGlobal.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_districts.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("keyword", keyword);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                List<String> newList = new ArrayList<String>();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String district_id = jsonOffer.getString("district_id");
                                            newList.add(district_id);
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.SearchDistrictByTextIds, arrSelectedLocationList.toString());
                                        editor.commit();
                                    }
                                } else {
                                    final List<String> arrSelectedLocationList = newList;
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Enums.SearchDistrictByTextIds, arrSelectedLocationList.toString());
                                    editor.commit();
                                }
                                searchInFilterByLocation(final_ctx, view_group);
                                // progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Filter Location", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void searchInFilterByLocation(Context ctx, final ViewGroup view_group) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_districts.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    LinearLayout llFilterByLocationChosen = (LinearLayout) view_group.findViewById(R.id.filter__by_location_chosen);
                                    LinearLayout llFilterByLocationOptions = (LinearLayout) view_group.findViewById(R.id.filter_by_location_options);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
                                        List<String> newList = new ArrayList<String>();
                                        if (str.equals("") == false) {
                                            str = str.substring(1, str.length() - 1);
                                            str.replace(" ", "");
                                            String[] str_arr = str.split(",");
                                            for (int s = 0; s < str_arr.length; s++) {
                                                if (str_arr[s].trim().equals("") == false) {
                                                    newList.add(str_arr[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        String str_districts = sharedpreferences.getString(Enums.SearchDistrictByTextIds, "");

                                        List<String> newList_districts = new ArrayList<String>();
                                        if (str_districts.equals("") == false) {
                                            str_districts = str_districts.substring(1, str_districts.length() - 1);
                                            str_districts.replace(" ", "");
                                            Log.i("Filtered Districts", str_districts);
                                            String[] str_arr_districts = str_districts.split(",");
                                            for (int s = 0; s < str_arr_districts.length; s++) {
                                                if (str_arr_districts[s].trim().equals("") == false) {
                                                    newList_districts.add(str_arr_districts[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrFilteredDistricts = newList_districts;

                                        llFilterByLocationChosen.removeAllViews();
                                        llFilterByLocationOptions.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String district_id = jsonOffer.getString("district_id");
                                            String name = jsonOffer.getString("name");
                                            ViewGroup viewFilterByLocationChosen = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_chosen, null);
                                            ViewGroup viewFilterByLocationOption = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_option, null);
                                            final RelativeLayout rlChosen1 = (RelativeLayout) viewFilterByLocationChosen.findViewById(R.id.rlChosen1);
                                            final TextView txtChosen1 = (TextView) viewFilterByLocationChosen.findViewById(R.id.txtChosen1);
                                            final View vChosen1 = (View) viewFilterByLocationChosen.findViewById(R.id.vChosen1);
                                            final RelativeLayout rlOption1 = (RelativeLayout) viewFilterByLocationOption.findViewById(R.id.rlOption1);
                                            final View vOption1 = (View) viewFilterByLocationOption.findViewById(R.id.vOption1);
                                            final TextView txtOption1 = (TextView) viewFilterByLocationOption.findViewById(R.id.txtOption1);
                                            txtChosen1.setText(name);
                                            txtOption1.setText(name);
                                            rlChosen1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlChosen1.setVisibility(View.GONE);
                                                                                 vChosen1.setVisibility(View.GONE);
                                                                                 rlOption1.setVisibility(View.VISIBLE);
                                                                                 vOption1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.remove(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterLocationsSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );
                                            rlOption1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlOption1.setVisibility(View.GONE);
                                                                                 vOption1.setVisibility(View.GONE);
                                                                                 rlChosen1.setVisibility(View.VISIBLE);
                                                                                 vChosen1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.add(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterLocationsSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );


                                            if (arrSelectedLocationList.contains(district_id) == true) {
                                                rlOption1.setVisibility(View.GONE);
                                                vOption1.setVisibility(View.GONE);
                                                rlChosen1.setVisibility(View.VISIBLE);
                                                vChosen1.setVisibility(View.VISIBLE);
                                            }

                                            if (arrFilteredDistricts.contains(district_id) == false) {
                                                rlOption1.setVisibility(View.GONE);
                                                vOption1.setVisibility(View.GONE);
                                                Log.i("Sel 2", district_id);
                                            }

                                            llFilterByLocationChosen.addView(viewFilterByLocationChosen);
                                            llFilterByLocationOptions.addView(viewFilterByLocationOption);
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Filter Location", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadFilterByCategory(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_categories.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    LinearLayout llFilterByLocationChosen = (LinearLayout) view_group.findViewById(R.id.filter_by_category_chosen);
                                    LinearLayout llFilterByLocationOptions = (LinearLayout) view_group.findViewById(R.id.filter_by_category_options);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
                                        List<String> newList = new ArrayList<String>();
                                        if (str.equals("") == false) {
                                            str = str.substring(1, str.length() - 1);
                                            str.replace(" ", "");
                                            Log.i("str", str);
                                            String[] str_arr = str.split(",");
                                            for (int s = 0; s < str_arr.length; s++) {
                                                if (str_arr[s].trim().equals("") == false) {
                                                    newList.add(str_arr[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String district_id = jsonOffer.getString("category_id");
                                            String name = jsonOffer.getString("name");
                                            ViewGroup viewFilterByLocationChosen = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_chosen, null);
                                            ViewGroup viewFilterByLocationOption = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_option, null);
                                            final RelativeLayout rlChosen1 = (RelativeLayout) viewFilterByLocationChosen.findViewById(R.id.rlChosen1);
                                            final TextView txtChosen1 = (TextView) viewFilterByLocationChosen.findViewById(R.id.txtChosen1);
                                            final View vChosen1 = (View) viewFilterByLocationChosen.findViewById(R.id.vChosen1);
                                            final RelativeLayout rlOption1 = (RelativeLayout) viewFilterByLocationOption.findViewById(R.id.rlOption1);
                                            final View vOption1 = (View) viewFilterByLocationOption.findViewById(R.id.vOption1);
                                            final TextView txtOption1 = (TextView) viewFilterByLocationOption.findViewById(R.id.txtOption1);
                                            txtChosen1.setText(name);
                                            txtOption1.setText(name);
                                            rlChosen1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlChosen1.setVisibility(View.GONE);
                                                                                 vChosen1.setVisibility(View.GONE);
                                                                                 rlOption1.setVisibility(View.VISIBLE);
                                                                                 vOption1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.remove(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterCategoriesSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );
                                            rlOption1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlOption1.setVisibility(View.GONE);
                                                                                 vOption1.setVisibility(View.GONE);
                                                                                 rlChosen1.setVisibility(View.VISIBLE);
                                                                                 vChosen1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.add(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterCategoriesSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );

                                            Log.i("Despacito 1", newList + "");
                                            if (arrSelectedLocationList.contains(district_id) == true) {
                                                rlOption1.setVisibility(View.GONE);
                                                vOption1.setVisibility(View.GONE);
                                                rlChosen1.setVisibility(View.VISIBLE);
                                                vChosen1.setVisibility(View.VISIBLE);
                                            }

                                            llFilterByLocationChosen.addView(viewFilterByLocationChosen);
                                            llFilterByLocationOptions.addView(viewFilterByLocationOption);
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Filter Location", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void getSearchInFilterByCategoryResults(Context ctx, final ViewGroup view_group, String keyword) {

        try {
            progressDialogGlobal = new SpotsDialog(ctx, R.style.Custom);
            progressDialogGlobal.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_categories.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("keyword", keyword);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                List<String> newList = new ArrayList<String>();
                                if (strStatus.equals("1")) {
                                    LinearLayout llFilterByLocationChosen = (LinearLayout) view_group.findViewById(R.id.filter_by_category_chosen);
                                    LinearLayout llFilterByLocationOptions = (LinearLayout) view_group.findViewById(R.id.filter_by_category_options);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String category_id = jsonOffer.getString("category_id");
                                            newList.add(category_id);
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.SearchCategoryByTextIds, arrSelectedLocationList.toString());
                                        editor.commit();
                                    }
                                } else {
                                    final List<String> arrSelectedLocationList = newList;
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Enums.SearchCategoryByTextIds, arrSelectedLocationList.toString());
                                    editor.commit();
                                }
                                searchInFilterByCategory(final_ctx, view_group);
                            } catch (Exception ex) {
                                Log.i("Load Filter Location", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void searchInFilterByCategory(Context ctx, final ViewGroup view_group) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_categories.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                String strMessage = jsonObj.getString("message");
                                if (strStatus.equals("1")) {
                                    LinearLayout llFilterByLocationChosen = (LinearLayout) view_group.findViewById(R.id.filter_by_category_chosen);
                                    LinearLayout llFilterByLocationOptions = (LinearLayout) view_group.findViewById(R.id.filter_by_category_options);
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
                                        List<String> newList = new ArrayList<String>();
                                        if (str.equals("") == false) {
                                            str = str.substring(1, str.length() - 1);
                                            str.replace(" ", "");
                                            Log.i("str", str);
                                            String[] str_arr = str.split(",");
                                            for (int s = 0; s < str_arr.length; s++) {
                                                if (str_arr[s].trim().equals("") == false) {
                                                    newList.add(str_arr[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        String str_districts = sharedpreferences.getString(Enums.SearchCategoryByTextIds, "");

                                        List<String> newList_districts = new ArrayList<String>();
                                        if (str_districts.equals("") == false) {
                                            str_districts = str_districts.substring(1, str_districts.length() - 1);
                                            str_districts.replace(" ", "");
                                            Log.i("Filtered Districts", str_districts);
                                            String[] str_arr_districts = str_districts.split(",");
                                            for (int s = 0; s < str_arr_districts.length; s++) {
                                                if (str_arr_districts[s].trim().equals("") == false) {
                                                    newList_districts.add(str_arr_districts[s].trim());
                                                }
                                            }
                                        }
                                        final List<String> arrFilteredDistricts = newList_districts;

                                        llFilterByLocationChosen.removeAllViews();
                                        llFilterByLocationOptions.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String district_id = jsonOffer.getString("category_id");
                                            String name = jsonOffer.getString("name");
                                            ViewGroup viewFilterByLocationChosen = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_chosen, null);
                                            ViewGroup viewFilterByLocationOption = (ViewGroup) inflater.inflate(R.layout.activity_filterbylocation_option, null);
                                            final RelativeLayout rlChosen1 = (RelativeLayout) viewFilterByLocationChosen.findViewById(R.id.rlChosen1);
                                            final TextView txtChosen1 = (TextView) viewFilterByLocationChosen.findViewById(R.id.txtChosen1);
                                            final View vChosen1 = (View) viewFilterByLocationChosen.findViewById(R.id.vChosen1);
                                            final RelativeLayout rlOption1 = (RelativeLayout) viewFilterByLocationOption.findViewById(R.id.rlOption1);
                                            final View vOption1 = (View) viewFilterByLocationOption.findViewById(R.id.vOption1);
                                            final TextView txtOption1 = (TextView) viewFilterByLocationOption.findViewById(R.id.txtOption1);
                                            txtChosen1.setText(name);
                                            txtOption1.setText(name);
                                            rlChosen1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlChosen1.setVisibility(View.GONE);
                                                                                 vChosen1.setVisibility(View.GONE);
                                                                                 rlOption1.setVisibility(View.VISIBLE);
                                                                                 vOption1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.remove(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterCategoriesSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );
                                            rlOption1.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 rlOption1.setVisibility(View.GONE);
                                                                                 vOption1.setVisibility(View.GONE);
                                                                                 rlChosen1.setVisibility(View.VISIBLE);
                                                                                 vChosen1.setVisibility(View.VISIBLE);
                                                                                 arrSelectedLocationList.add(district_id);
                                                                                 SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                                 editor.putString(Enums.FilterCategoriesSelected, arrSelectedLocationList.toString());
                                                                                 editor.commit();
                                                                             }
                                                                         }
                                            );

                                            if (arrSelectedLocationList.contains(district_id) == true) {
                                                rlOption1.setVisibility(View.GONE);
                                                vOption1.setVisibility(View.GONE);
                                                rlChosen1.setVisibility(View.VISIBLE);
                                                vChosen1.setVisibility(View.VISIBLE);
                                            }

                                            if (arrFilteredDistricts.contains(district_id) == false) {
                                                rlOption1.setVisibility(View.GONE);
                                                vOption1.setVisibility(View.GONE);
                                            }

                                            llFilterByLocationChosen.addView(viewFilterByLocationChosen);
                                            llFilterByLocationOptions.addView(viewFilterByLocationOption);
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
                            } catch (Exception ex) {
                                Log.i("Load Filter Location", ex + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void displayCategoriesGo(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.CategoriesSelectedForSearch, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", "");
            hash_map.put("keyword", "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                Log.i("responsecat", response + "");
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //    .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                                Log.i("Categoriesgo", exception + "");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void displayFilterByLocationView(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }

            hash_map.put("date", formattedDate);
            hash_map.put("district_ids", strIDS);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", "");
            hash_map.put("keyword", "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //   .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void displayFilterByCategoriesView(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", "");
            hash_map.put("keyword", "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //    .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void displayNearbyOffersListing(Context ctx, String latitude, String longitude, final ViewGroup view_group){

        try {
            if (Functionalities.isNetworkAvailable(ctx) == true) {
                final Context final_ctx = ctx;
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "nearby_offers.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                hash_map.put("date", formattedDate);
                hash_map.put("latitude", latitude);
                hash_map.put("longitude", longitude);


                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    if (strStatus.equals("1")) {
                                        JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                        int n = jsonData.length();
                                        if (n > 0) {
                                            for (int i = 0; i < n; ++i) {
                                                try {
                                                    ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                    ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                    LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                    llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.nearbylist_panels_enc);
                                                    TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                    // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                    JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                    String retailer_name = jsonOffer.getString("retailer_name");
                                                    final String offer_id = jsonOffer.getString("offer_id");
                                                    String offer_name = jsonOffer.getString("offer_name");
                                                    String issue_date = jsonOffer.getString("issue_date");
                                                    String expiry_date = jsonOffer.getString("expiry_date");
                                                    String validity = jsonOffer.getString("validity");
                                                    String frequency = jsonOffer.getString("frequency");
                                                    String featured = jsonOffer.getString("featured_cropped");
                                                    Log.i("featured", featured);

                                                    String category = jsonOffer.getString("category");
                                                    TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                    txtOfferPanelCategory.setText(category);
                                                    TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                    TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                    TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                    ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                    final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                    txtOfferPanelRetailerName.setText(retailer_name);
                                                    txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                    txtOfferPanelUnlimited.setText(frequency);
                                                    txtOfferName.setText(offer_name);
                                                    try {
                                                        // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                        //    .execute(featured);
                                                        Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                            @Override
                                                            public void onSuccess() {
                                                                rlProgBar.setVisibility(View.GONE);
                                                            }

                                                            @Override
                                                            public void onError(Exception e) {

                                                            }
                                                        });
                                                    } catch (Exception exception) {
                                                    }
                                                    ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                            Log.i("Clicked_Offer_Id", offer_id);
                                                            intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                            final_ctx.startActivity(intent);
                                                        }
                                                    });

                                                    llOfferPanelEnc.addView(viewOfferPanel);
                                                    llOfferPanelEnc.addView(viewSeparator15);
                                                } catch (Exception ex) {
                                                    Log.i("nearbo", ex + "");
                                                }
                                            }
                                        }
                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Nearby Deals");
                                        alertDialog.setMessage("Please check your internet connection.");
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
                                } catch (JSONException exception) {

                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);
            }
            else {
                final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("Nearby Deals");
                alertDialog.setMessage("Your internet connection is not enabled");
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

        }
        catch (Exception ex){
        }

    }

    public void displayFilterByLocationAndCategoriesView(Context ctx, final ViewGroup view_group){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
            }
            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", districtIds);
            hash_map.put("keyword", "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    //new DownloadImageTask(ivOfferPanel, final_ctx)
                                                       //  .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void filterOffersInFilterScreenLocation(Context ctx, final ViewGroup view_group, String keyword){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
                Log.i("strDistrictsIds", districtIds);
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", "");
            hash_map.put("district_ids", districtIds);
            hash_map.put("keyword", keyword);
            Log.i("keyword", keyword);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //     .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    } else {
                                        llOfferPanelEnc.removeAllViews();
                                    }
                                }
                                else {
                                    llOfferPanelEnc.removeAllViews();
                                }
                                for (int m = 0; m < 4; m++){
                                    ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                    ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                    viewOfferPanel.setVisibility(View.INVISIBLE);
                                    viewSeparator15.setVisibility(View.INVISIBLE);
                                    llOfferPanelEnc.addView(viewOfferPanel);
                                    llOfferPanelEnc.addView(viewSeparator15);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex) {}

    }

    public void filterOffersInFilterScreenCategoriesGo(Context ctx, final ViewGroup view_group, String keyword){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.CategoriesSelectedForSearch, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", "");
            hash_map.put("keyword", keyword);


            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //     .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    } else {
                                        llOfferPanelEnc.removeAllViews();
                                    }
                                }
                                else {
                                    llOfferPanelEnc.removeAllViews();
                                }
                                for (int m = 0; m < 4; m++){
                                    ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                    ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                    viewOfferPanel.setVisibility(View.INVISIBLE);
                                    viewSeparator15.setVisibility(View.INVISIBLE);
                                    llOfferPanelEnc.addView(viewOfferPanel);
                                    llOfferPanelEnc.addView(viewSeparator15);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void filterOffersInFilterScreenCategories(Context ctx, final ViewGroup view_group, String keyword){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", "");
            hash_map.put("keyword", keyword);


            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask(ivOfferPanel, final_ctx)
                                                    //     .execute(featured);
                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    } else {
                                        llOfferPanelEnc.removeAllViews();
                                    }
                                }
                                else {
                                    llOfferPanelEnc.removeAllViews();
                                }
                                for (int m = 0; m < 4; m++){
                                    ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                    ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                    viewOfferPanel.setVisibility(View.INVISIBLE);
                                    viewSeparator15.setVisibility(View.INVISIBLE);
                                    llOfferPanelEnc.addView(viewOfferPanel);
                                    llOfferPanelEnc.addView(viewSeparator15);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void filterOffersInFilterScreenLocationAndCategories(Context ctx, final ViewGroup view_group, String keyword){

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
            }

            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", districtIds);
            hash_map.put("keyword", keyword);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                if (strStatus.equals("1")) {
                                    Log.i("carmel", response + "");
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                // LinearLayout llOfferPanel = (LinearLayout) viewOfferPanel.findViewById(R.id.offer_panel);
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                String retailer_name = jsonOffer.getString("retailer_name");
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                String offer_name = jsonOffer.getString("offer_name");
                                                String issue_date = jsonOffer.getString("issue_date");
                                                String expiry_date = jsonOffer.getString("expiry_date");
                                                String validity = jsonOffer.getString("validity");
                                                String frequency = jsonOffer.getString("frequency");
                                                String featured = jsonOffer.getString("featured_cropped");
                                                Log.i("featured", featured);

                                                String category = jsonOffer.getString("category");
                                                TextView txtOfferPanelCategory = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_category);
                                                txtOfferPanelCategory.setText(category);
                                                TextView txtOfferPanelRetailerName = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_retailer_name);
                                                TextView txtOfferPanelDate = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_date);
                                                TextView txtOfferPanelUnlimited = (TextView) viewOfferPanel.findViewById(R.id.offer_panel_unlimited);
                                                ivOfferPanel = (ImageView) viewOfferPanel.findViewById(R.id.offer_panel_image);
                                                final RelativeLayout rlProgBar = (RelativeLayout) viewOfferPanel.findViewById(R.id.prog_bar);
                                                txtOfferPanelRetailerName.setText(retailer_name);
                                                txtOfferPanelDate.setText(issue_date + " - " + expiry_date);
                                                txtOfferPanelUnlimited.setText(frequency);
                                                txtOfferName.setText(offer_name);
                                                try {
                                                    // new DownloadImageTask1(ivOfferPanel, final_ctx, rlProgBar)
                                                      //.execute(featured);

                                                    Picasso.get().load(featured).into(ivOfferPanel, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            rlProgBar.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {

                                                        }
                                                    });

                                                } catch (Exception exception) {
                                                }
                                                ivOfferPanel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                        Log.i("Clicked_Offer_Id", offer_id);
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    } else {
                                        llOfferPanelEnc.removeAllViews();
                                    }

                                }
                                else {
                                    llOfferPanelEnc.removeAllViews();
                                }
                                for (int m = 0; m < 4; m++){
                                    ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                    ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                    viewOfferPanel.setVisibility(View.INVISIBLE);
                                    viewSeparator15.setVisibility(View.INVISIBLE);
                                    llOfferPanelEnc.addView(viewOfferPanel);
                                    llOfferPanelEnc.addView(viewSeparator15);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }catch (Exception ex){}

    }

    public void loadFilterActivity(Context ctx, final ViewGroup view_group){

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_offers.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            String strIDS = sharedpreferences.getString(Enums.FilterCategoriesSelected, "");
            String districtIds = sharedpreferences.getString(Enums.FilterLocationsSelected, "");
            if (strIDS.equals("") == false) {
                strIDS = strIDS.substring(1, strIDS.length() - 1);
                strIDS.replace(" ", "");
                Log.i("strCategoriesGo", strIDS);
            }
            if (districtIds.equals("") == false) {
                districtIds = districtIds.substring(1, districtIds.length() - 1);
                districtIds.replace(" ", "");
                Log.i("strDistrictsGo", districtIds);
            }
            hash_map.put("date", formattedDate);
            hash_map.put("country_code", Config.COUNTRY_CODE);
            hash_map.put("category_ids", strIDS);
            hash_map.put("district_ids", districtIds);
            hash_map.put("keyword", "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                TextView txtNumberOfOffers = (TextView) view_group.findViewById(R.id.number_of_offers);
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        if (n == 1) {
                                            txtNumberOfOffers.setText(n + " offer");
                                        } else {
                                            txtNumberOfOffers.setText(n + " offers");
                                        }
                                    } else {
                                        txtNumberOfOffers.setText(n + " offers");
                                    }
                                }
                                else {
                                    txtNumberOfOffers.setText("0 offers");
                                }
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);

        }
        catch (Exception ex){}

    }

    public void displayNearbyOffersMap(Context ctx, String latitude, String longitude, GoogleMap map){

        try {
            if (Functionalities.isNetworkAvailable(ctx) == true) {
                final Context final_ctx = ctx;
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "nearby_offers.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                hash_map.put("date", formattedDate);
                hash_map.put("latitude", latitude);
                hash_map.put("longitude", longitude);
                final GoogleMap mMap = map;


                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    if (strStatus.equals("1")) {
                                        JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                        int n = jsonData.length();
                                        if (n > 0) {
                                            Marker[] markers1 = new Marker[n];
                                            for (int i = 0; i < n; ++i) {
                                                try {
                                                    Log.i("pa2useyi 0", "0");
                                                    JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                    final String offer_id = jsonOffer.getString("offer_id");
                                                    final String offer_name = jsonOffer.getString("offer_name");
                                                    final String retailer_name = jsonOffer.getString("retailer_name");
                                                    final String featured = jsonOffer.getString("featured_cropped");
                                                    Log.i("pa2useyi 1", featured);
                                                    JSONArray jsonBranches = (JSONArray) jsonOffer.get("branches");
                                                    JSONObject jsonBranch = jsonBranches.getJSONObject(0);
                                                    final String latitude = jsonBranch.getString("latitude");
                                                    final String longitude = jsonBranch.getString("longitude");
                                                    Log.i("pa2useyi 2", "1");

                                                    LatLng _latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                                    final Marker _marker = mMap.addMarker(new
                                                            MarkerOptions().position(_latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                                                            .title(offer_name + "," + retailer_name + "," + featured + "," + offer_id)
                                                    );
                                                    _marker.setSnippet("no");
                                                    markers1[i] = _marker;
                                                    float _zoom = 12;
                                                /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                                                {
                                                    @Override
                                                    public boolean onMarkerClick(Marker arg0) {
                                                        if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
                                                            Toast.makeText(final_ctx, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                                                        return true;
                                                    }
                                                }); */

                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_latlng, _zoom));
                                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                                        @Override
                                                        public View getInfoWindow(Marker arg0) {
                                                            inflater = (LayoutInflater) final_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                            View layout = inflater.inflate(R.layout.activity_info_window,
                                                                    null);
                                                            final RelativeLayout rlProgBar = (RelativeLayout) layout.findViewById(R.id.card_prog_bar);
                                                            String[] off_name = arg0.getTitle().split(",");
                                                            TextView txtOfferName = (TextView) layout.findViewById(R.id.info_window_offer_name);
                                                            txtOfferName.setText(off_name[0]);
                                                            TextView txtRetailerName = (TextView) layout.findViewById(R.id.info_window_retailer_name);
                                                            txtRetailerName.setText(off_name[1]);
                                                            final String off2 = off_name[2];
                                                            final ImageView ivPicture = (ImageView) layout.findViewById(R.id.info_window_image);
                                                            // new DownloadImageTask(ivPicture, final_ctx)
                                                            //  .execute(off_name[2]);
                                                            final Marker marker = arg0;
                                                            Picasso.get().load(off_name[2]).into(ivPicture, new Callback() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    rlProgBar.setVisibility(View.GONE);
                                                                    if (marker != null && marker.isInfoWindowShown()) {
                                                                        marker.hideInfoWindow();
                                                                        marker.showInfoWindow();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onError(Exception e) {

                                                                }
                                                            });
                                                            // arg0.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_active));
                                                            if (arg0.isInfoWindowShown()) {
                                                                return null;
                                                            } else {
                                                                return layout;
                                                            }
                                                        }

                                                        @Override
                                                        public View getInfoContents(Marker arg0) {
                                                            return null;
                                                        }

                                                    });

                                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                        @Override
                                                        public void onInfoWindowClick(Marker arg1) {
                                                            String[] off_name = arg1.getTitle().split(",");
                                                            Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                            intent.putExtra("SELECTED_OFFER_ID", off_name[3]);
                                                            final_ctx.startActivity(intent);
                                                        }
                                                    });

                                            /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                                                @Override
                                                public boolean onMarkerClick(Marker marker) {
                                                    if(marker.isInfoWindowShown() == true){
                                                        marker.hideInfoWindow();
                                                    }
                                                    return true;
                                                }
                                            }); */

                                                } catch (Exception ex) {
                                                }

                                            }
                                    /*
                                    final Marker[] markers = markers1;
                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            Log.i("Mon ami", "");
                                            for (int j = 0; j < markers.length; j++) {
                                                markers[j].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
                                            }
                                            if (marker.getSnippet() == "no") {
                                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_active));
                                                marker.setSnippet("yes");
                                            }
                                            else {
                                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
                                                marker.setSnippet("no");
                                                marker.hideInfoWindow();
                                            }
                                            return true;
                                        }
                                    }); */
                                        }
                                    } else {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(final_ctx).create();
                                        alertDialog.setTitle("Nearby Deals");
                                        alertDialog.setMessage("Please check your internet connection.");
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
                                } catch (JSONException exception) {
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                queue.add(jsonObjectReq);
            }
            else {
                final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                alertDialog.setTitle("Nearby Deals");
                alertDialog.setMessage("Your internet connection is not enabled");
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
        }
        catch (Exception ex){}

    }

    public void loadAboutUsEng(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();
            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("1") == true) {
                                                    final String description = jsonOffer.getString("description");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.privileb_about);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
    }
    catch (Exception ex){}

    }

    public void loadAboutUsAr(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("1") == true) {
                                                    final String description = jsonOffer.getString("description_ar");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.privileb_about);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadBenefitsEng(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("4") == true) {
                                                    final String description = jsonOffer.getString("description");
                                                    Log.i("benefitsen", description);
                                                    TextView wv = (TextView) view_group.findViewById(R.id.webview_benefits);
                                                    wv.setTextDirection(View.TEXT_DIRECTION_LTR);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadBenefitsAr(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("4") == true) {
                                                    final String description = jsonOffer.getString("description_ar");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.webview_benefits);
                                                    wv.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadTermsAndConditionsEng(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("3") == true) {
                                                    final String description = jsonOffer.getString("description");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.webview_terms);
                                                    wv.setTextDirection(View.TEXT_DIRECTION_LTR);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadTermsAndConditionsAr(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("3") == true) {
                                                    final String description = jsonOffer.getString("description_ar");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.webview_terms);
                                                    wv.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadJoinOurFamily(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("5") == true) {
                                                    final String description = jsonOffer.getString("description");
                                                    TextView wv = (TextView) view_group.findViewById(R.id.privileb_join_our_family);
                                                    wv.setTextDirection(View.TEXT_DIRECTION_LTR);
                                                    Spanned result;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                        result = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
                                                    } else {
                                                        result = Html.fromHtml(description);
                                                    }
                                                    wv.setText(result);
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public void loadCharities(Context ctx, final ViewGroup view_group) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_static_pages.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("country_code", Config.COUNTRY_CODE);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String static_page_id = jsonOffer.getString("id");
                                                if (static_page_id.equals("7") == true) {
                                                    final String description = jsonOffer.getString("description");
                                                    WebView wv = (WebView) view_group.findViewById(R.id.charities_text);
                                                    final String mimeType = "text/html";
                                                    final String encoding = "UTF-8";
                                                    wv.loadDataWithBaseURL("", description, mimeType, encoding, "");
                                                }
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();;
                            } catch (JSONException exception) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(jsonObjectReq);
        }
        catch (Exception ex){}

    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Context ctx;

        public DownloadImageTask(ImageView bmImage, Context ctx) {
            this.bmImage = bmImage;
            this.ctx = ctx;
        }

        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static class DownloadImageTask1 extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Context ctx;
        RelativeLayout pd;

        public DownloadImageTask1(ImageView bmImage, Context ctx, RelativeLayout progress_dialog) {
            this.bmImage = bmImage;
            this.ctx = ctx;
            this.pd = progress_dialog;
        }

        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            pd.setVisibility(View.GONE);
        }
    }


    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError(Exception e) {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

    public static class DownloadImageTaskGone extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Context ctx;
        LinearLayout ll;

        public DownloadImageTaskGone(ImageView bmImage, Context ctx, LinearLayout ll) {
            this.bmImage = bmImage;
            this.ctx = ctx;
            this.ll = ll;
        }

        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            ll.setVisibility(View.GONE);
        }
    }

    public void requestPermission(Activity activity)
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CALL_PHONE))
        {
        }
        else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},Config.PERMISSION_REQUEST_CODE);
        }
    }


}