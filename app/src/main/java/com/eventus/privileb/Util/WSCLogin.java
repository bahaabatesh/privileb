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
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eventus.privileb.General.Config;
import com.eventus.privileb.General.Enums;
import com.eventus.privileb.General.Functionalities;
import com.eventus.privileb.HomeActivity;
import com.eventus.privileb.ImagePopupActivity;
import com.eventus.privileb.OfferActivity;
import com.eventus.privileb.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import dmax.dialog.SpotsDialog;

public class WSCLogin {

    LayoutInflater inflater;
    ImageView ivOfferPanel;
    LinearLayout llOfferPanelEnc;
    SharedPreferences sharedpreferences;
    Marker new_marker;
    Button btn_location;
    AlertDialog progressDialogGlobal;
    private String current_offer;

    public void getUserFavoritesFeaturedDeals(final Context ctx, final ViewGroup view_group, String user_id, final String request_type, final String keyword) {

        try {
            progressDialogGlobal = new SpotsDialog(ctx, R.style.Custom);
            progressDialogGlobal.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_favorites_list.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("user_id", user_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                List<String> newListFull = new ArrayList<String>();
                                Log.i("responso", response + "");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String offer_id = jsonOffer.getString("offer_id");
                                            final String favorite_id = jsonOffer.getString("favorite_id");
                                            newList.add(offer_id);
                                            newListFull.add(offer_id + "/" + favorite_id);
                                        }
                                        final List<String> arrSelectedLocationList = newList;
                                        final List<String> arrSelectedLocationListFull = newListFull;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.UserFavoritesList, arrSelectedLocationList.toString());
                                        editor.putString(Enums.UserOffersFavoritesList, arrSelectedLocationListFull.toString());
                                        Log.i("UserOffersFavoritesList", arrSelectedLocationListFull.toString());
                                        editor.commit();
                                    }
                                } else {
                                    final List<String> arrSelectedLocationList = newList;
                                    final List<String> arrSelectedLocationListFull = newListFull;
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Enums.UserFavoritesList, arrSelectedLocationList.toString());
                                    editor.putString(Enums.UserOffersFavoritesList, arrSelectedLocationListFull.toString());
                                    editor.commit();
                                }
                                if (request_type.equals("featured_deals") == true) {
                                    displayFeaturedOffers(ctx, view_group);
                                } else if (request_type.equals("all_deals") == true) {
                                    displayAllDeals(ctx, view_group);
                                } else if (request_type.equals("categories_go") == true) {
                                    displayCategoriesGo(ctx, view_group);
                                } else if (request_type.equals("filter_by_location") == true) {
                                    displayFilterByLocationView(ctx, view_group);
                                } else if (request_type.equals("filter_by_categories") == true) {
                                    displayFilterByCategoriesView(ctx, view_group);
                                } else if (request_type.equals("filter_by_location_and_categories") == true) {
                                    displayFilterByLocationAndCategoriesView(ctx, view_group);
                                } else if (request_type.equals("filterscreen_location") == true) {
                                    filterOffersInFilterScreenLocation(ctx, view_group, keyword);
                                } else if (request_type.equals("filterscreen_categories") == true) {
                                    filterOffersInFilterScreenCategories(ctx, view_group, keyword);
                                } else if (request_type.equals("filterscreen_location_and_categories") == true) {
                                    filterOffersInFilterScreenLocationAndCategories(ctx, view_group, keyword);
                                }
                                else if (request_type.equals("filter_categories_go") == true) {
                                    filterOffersInFilterScreenCategoriesGo(ctx, view_group, keyword);
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

    public void displayFeaturedOffers(Context ctx, final ViewGroup view_group){

        try {
            final Context final_ctx = ctx;
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
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "featured_deals");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                                Log.i("mama", ex + "");
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
                            } catch (JSONException exception) {
                                Log.i("mama", exception + "");
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

    public void addToFavorites(Context ctx, final ImageView btn_favorite, final String offer_id, final String request_type) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "add_to_favorites.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            Log.i("Usero", sharedpreferences.getLong(Enums.UserId, 0) + "," + offer_id + "," + formattedDate);
            hash_map.put("user_id", sharedpreferences.getLong(Enums.UserId, 0) + "");
            hash_map.put("offer_id", offer_id);
            hash_map.put("datetime", formattedDate);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                if (strStatus.equals("1")) {
                                    if (request_type.equals("deals") == true) {
                                        btn_favorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // unfavorite call
                                                getUserFavoritesId(final_ctx, btn_favorite, offer_id, "deals");
                                            }
                                        });
                                    } else if (request_type.equals("offer_details") == true) {
                                        btn_favorite.setImageBitmap(null);
                                        btn_favorite.setBackgroundResource(R.drawable.offer_favorite_selected1);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // unfavorite call
                                                getUserFavoritesId(final_ctx, btn_favorite, offer_id, "offer_details");
                                            }
                                        });
                                    }
                                    else if (request_type.equals("favorites") == true) {
                                        int id = final_ctx.getResources().getIdentifier("drawable/" + "favorited", null, final_ctx.getPackageName());
                                        btn_favorite.setImageResource(id);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // unfavorite call
                                                getUserFavoritesId(final_ctx, btn_favorite, offer_id, "favorites");
                                            }
                                        });
                                    }
                                } else {

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

    public void removeFromFavorites(Context ctx, final ImageView btn_favorite, final String offer_id, String favorite_id, final String request_type) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "remove_from_favorites.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            hash_map.put("favorite_id", favorite_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                if (strStatus.equals("1")) {
                                    if (request_type.equals("deals") == true) {
                                        btn_favorite.setBackgroundResource(R.drawable.deals_favorite1);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // favorite call
                                                addToFavorites(final_ctx, btn_favorite, offer_id, "deals");
                                            }
                                        });
                                    } else if (request_type.equals("offer_details") == true) {
                                        btn_favorite.setBackgroundResource(R.drawable.offer_favorite1);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // favorite call
                                                addToFavorites(final_ctx, btn_favorite, offer_id, "offer_details");
                                            }
                                        });
                                    }
                                    else if (request_type.equals("favorites") == true) {
                                        int id = final_ctx.getResources().getIdentifier("drawable/" + "not_favorited", null, final_ctx.getPackageName());
                                        btn_favorite.setImageResource(id);
                                        btn_favorite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // favorite call
                                                addToFavorites(final_ctx, btn_favorite, offer_id, "favorites");
                                            }
                                        });
                                    }
                                } else {

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

    public void getUserFavoritesId(final Context ctx, final ImageView btn_favorite, final String offer_id, final String request_type) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_favorite_by_userid_offerid.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            hash_map.put("offer_id", offer_id);
            hash_map.put("user_id", sharedpreferences.getLong(Enums.UserId, 0) + "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                List<String> newListFull = new ArrayList<String>();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        JSONObject jsonOffer = jsonData.getJSONObject(0);
                                        String favorite_id = jsonOffer.getString("favorite_id");
                                        removeFromFavorites(ctx, btn_favorite, offer_id, favorite_id, request_type);
                                    }
                                } else {

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

    public void checkOfferAddedToFavorite(final Context ctx, final ViewGroup view_group, final String offer_id, final GoogleMap googleMap) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_favorite_by_userid_offerid.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            hash_map.put("offer_id", offer_id);
            hash_map.put("user_id", sharedpreferences.getLong(Enums.UserId, 0) + "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                List<String> newListFull = new ArrayList<String>();
                                boolean is_fav = false;
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        is_fav = true;
                                    }
                                } else {
                                    is_fav = false;
                                }
                                displayOfferDetails(final_ctx, view_group, offer_id, googleMap, is_fav);
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
        catch (Exception ex ){}

    }

    public void displayOfferDetails(Context ctx, final ViewGroup view_group, String offer_id, final GoogleMap mMap, final boolean is_fav){

        try {
            final Context final_ctx = ctx;
            final WSC _wsc = new WSC();
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
                                final String offer_url = jsonOffer.getString("url");


                                LatLng _latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                new_marker = mMap.addMarker(new
                                        MarkerOptions().position(_latlng).title(location));
                                final float _zoom = 10;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_latlng, _zoom));
                                Log.i("inside", "inside");

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
                                final ImageView ivFavoriteThisOffer = (ImageView) view_group.findViewById(R.id.favorite_this_offer);
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
                                                            _wsc.requestPermission(final_activity);
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
                                txtRetailerName.setVisibility(View.GONE);
                                ivFavoriteThisOffer.setVisibility(View.VISIBLE);
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
                                                                            _wsc.requestPermission(final_activity);
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
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });

                                    if (is_fav == true) {
                                        ivFavoriteThisOffer.setImageBitmap(null);
                                        ivFavoriteThisOffer.setBackgroundResource(R.drawable.offer_favorite_selected1);
                                        ivFavoriteThisOffer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // unfavorite call
                                                getUserFavoritesId(final_ctx, ivFavoriteThisOffer, offer_id, "offer_details");
                                            }
                                        });
                                    } else {
                                        ivFavoriteThisOffer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // favorite call
                                                addToFavorites(final_ctx, ivFavoriteThisOffer, offer_id, "offer_details");
                                            }
                                        });
                                    }

                                }
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

    public void displayAllDeals(Context ctx, final ViewGroup view_group){

        try {
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
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "all_deals");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
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

    public void displayCategoriesGo(Context ctx, final ViewGroup view_group){

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
                                String strStatus = jsonObj.getString("status_code");
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "categories_go");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
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

    public void displayFilterByLocationView(Context ctx, final ViewGroup view_group){

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
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "filter_by_location");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
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
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "filter_by_categories");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
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

    public void displayFilterByLocationAndCategoriesView(Context ctx, final ViewGroup view_group){

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
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.offer_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        // filter_by_location_and_categories
                                                        intent.putExtra("FROM_VIEW", "filter_by_location_and_categories");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }
                                }
                                progressDialogGlobal.dismiss();
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
            hash_map.put("category_ids", "");
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
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "filter_by_location");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

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
                                progressDialogGlobal.dismiss();
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
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "filter_by_categories");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

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
                                progressDialogGlobal.dismiss();
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

    public void filterOffersInFilterScreenCategoriesGo(Context ctx, final ViewGroup view_group, String keyword){

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
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "categories_go");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

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
                                progressDialogGlobal.dismiss();
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
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        llOfferPanelEnc.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);
                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("FROM_VIEW", "filter_by_location_and_categories");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

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
                                progressDialogGlobal.dismiss();
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

    public void loadFavorites(final Context ctx, final ViewGroup view_group, String user_id) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_favorites_list.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("user_id", user_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                List<String> newListFull = new ArrayList<String>();
                                Log.i("responso", response + "");
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        LinearLayout llFavorites = (LinearLayout) view_group.findViewById(R.id.favorites_panel);
                                        llFavorites.removeAllViews();
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String offer_id = jsonOffer.getString("offer_id");
                                            final String favorite_id = jsonOffer.getString("favorite_id");
                                            String retailer_name = jsonOffer.getString("retailer_name");
                                            String offer_name = jsonOffer.getString("offer_name");
                                            String issue_date = jsonOffer.getString("issue_date");
                                            String expiry_date = jsonOffer.getString("expiry_date");
                                            String freq = jsonOffer.getString("frequency");
                                            ScrollView svFavorites = (ScrollView) view_group.findViewById(R.id.favorite_scrollview);
                                            svFavorites.setVisibility(View.VISIBLE);
                                            final ViewGroup viewFavoritePanel = (ViewGroup) inflater.inflate(R.layout.activity_favorite_panel, null);
                                            LinearLayout favoritePanelLeft = (LinearLayout) viewFavoritePanel.findViewById(R.id.favorite_panel1);
                                            TextView txtTitle = (TextView) viewFavoritePanel.findViewById(R.id.favorite_title);
                                            TextView txtSubtitle = (TextView) viewFavoritePanel.findViewById(R.id.favorite_subtitle);
                                            TextView txtDate = (TextView) viewFavoritePanel.findViewById(R.id.favorite_date);
                                            TextView txtValidity = (TextView) viewFavoritePanel.findViewById(R.id.favorite_validity);
                                            final ImageView ivFavorite = (ImageView) viewFavoritePanel.findViewById(R.id.favorite_image);
                                            final TextView txtFavoriteStatus = (TextView) viewFavoritePanel.findViewById(R.id.favorite_status);
                                            txtTitle.setText(offer_name);
                                            txtSubtitle.setText(retailer_name);
                                            txtDate.setText(issue_date + " - " + expiry_date);
                                            txtValidity.setText(freq);
                                            /*
                                            ivFavorite.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (txtFavoriteStatus.getText().equals("yes") == true) {
                                                        txtFavoriteStatus.setText("no");
                                                        removeFavorite(final_ctx, ivFavorite, favorite_id);
                                                    } else {
                                                        txtFavoriteStatus.setText("yes");
                                                        addToFavorite(final_ctx, ivFavorite, offer_id);
                                                    }
                                                }
                                            });
                                            */
                                            int id = final_ctx.getResources().getIdentifier("drawable/" + "favorited", null, final_ctx.getPackageName());
                                            ivFavorite.setImageResource(id);
                                            ivFavorite.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // unfavorite call
                                                        getUserFavoritesId(final_ctx, ivFavorite, offer_id, "favorites");
                                                    }
                                                });
                                            favoritePanelLeft.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                    intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                    intent.putExtra("FROM_VIEW", "favorites");
                                                    final_ctx.startActivity(intent);
                                                }
                                            });

                                            llFavorites.addView(viewFavoritePanel);
                                        }
                                    }
                                } else {
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                                Log.i("Load Favorites", exception + "");
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

    public void displayFavorites(final Context ctx, final ViewGroup view_group, String user_id, String keyword) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "search_favorites.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("user_id", user_id);
            hash_map.put("keyword", keyword);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                List<String> newList = new ArrayList<String>();
                                List<String> newListFull = new ArrayList<String>();
                                Log.i("responso", response + "");
                                LinearLayout llFavorites = (LinearLayout) view_group.findViewById(R.id.favorites_panel);
                                llFavorites.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        for (int i = 0; i < n; ++i) {
                                            JSONObject jsonOffer = jsonData.getJSONObject(i);
                                            final String offer_id = jsonOffer.getString("offer_id");
                                            String retailer_name = jsonOffer.getString("retailer_name");
                                            String offer_name = jsonOffer.getString("offer_name");
                                            String issue_date = jsonOffer.getString("issue_date");
                                            String expiry_date = jsonOffer.getString("expiry_date");
                                            String freq = jsonOffer.getString("frequency");
                                            ScrollView svFavorites = (ScrollView) view_group.findViewById(R.id.favorite_scrollview);
                                            svFavorites.setVisibility(View.VISIBLE);
                                            final ViewGroup viewFavoritePanel = (ViewGroup) inflater.inflate(R.layout.activity_favorite_panel, null);
                                            LinearLayout favoritePanelLeft = (LinearLayout) viewFavoritePanel.findViewById(R.id.favorite_panel1);
                                            TextView txtTitle = (TextView) viewFavoritePanel.findViewById(R.id.favorite_title);
                                            TextView txtSubtitle = (TextView) viewFavoritePanel.findViewById(R.id.favorite_subtitle);
                                            TextView txtDate = (TextView) viewFavoritePanel.findViewById(R.id.favorite_date);
                                            TextView txtValidity = (TextView) viewFavoritePanel.findViewById(R.id.favorite_validity);
                                            final ImageView ivFavorite = (ImageView) viewFavoritePanel.findViewById(R.id.favorite_image);
                                            final TextView txtFavoriteStatus = (TextView) viewFavoritePanel.findViewById(R.id.favorite_status);
                                            txtTitle.setText(offer_name);
                                            txtSubtitle.setText(retailer_name);
                                            txtDate.setText(issue_date + " - " + expiry_date);
                                            txtValidity.setText(freq);
                                            /*
                                            ivFavorite.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (txtFavoriteStatus.getText().equals("yes") == true) {
                                                        txtFavoriteStatus.setText("no");
                                                        removeFavorite(final_ctx, ivFavorite, favorite_id);
                                                    } else {
                                                        txtFavoriteStatus.setText("yes");
                                                        addToFavorite(final_ctx, ivFavorite, offer_id);
                                                    }
                                                }
                                            });
                                            */
                                            int id = final_ctx.getResources().getIdentifier("drawable/" + "favorited", null, final_ctx.getPackageName());
                                            ivFavorite.setImageResource(id);
                                            ivFavorite.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // unfavorite call
                                                    getUserFavoritesId(final_ctx, ivFavorite, offer_id, "favorites");
                                                }
                                            });
                                            favoritePanelLeft.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(final_ctx, OfferActivity.class);
                                                    intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                    intent.putExtra("FROM_VIEW", "favorites");
                                                    final_ctx.startActivity(intent);
                                                }
                                            });

                                            llFavorites.addView(viewFavoritePanel);
                                        }
                                    }
                                } else {
                                }
                                for (int m = 0; m < 4; m++){
                                    final ViewGroup viewFavoritePanel = (ViewGroup) inflater.inflate(R.layout.activity_favorite_panel, null);
                                    viewFavoritePanel.setVisibility(View.INVISIBLE);
                                    llFavorites.addView(viewFavoritePanel);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException exception) {
                                Log.i("Load Favorites", exception + "");
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

    public void removeFavorite(Context ctx, final ImageView favorite_panel, String favorite_id) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "remove_from_favorites.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("favorite_id", favorite_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("Remove res", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    Log.i("Remove res", "yes");
                                    int id = final_ctx.getResources().getIdentifier("drawable/" + "not_favorited", null, final_ctx.getPackageName());
                                    favorite_panel.setImageResource(id);
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

    public void addToFavorite(Context ctx, final ImageView favorite_panel, String offer_id) {

        try {
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "add_to_favorites.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            Log.i("Usero", sharedpreferences.getLong(Enums.UserId, 0) + "," + offer_id + "," + formattedDate);
            hash_map.put("user_id", sharedpreferences.getLong(Enums.UserId, 0) + "");
            hash_map.put("offer_id", offer_id);
            hash_map.put("datetime", formattedDate);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            Log.i("Add res", response + "");
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                if (strStatus.equals("1")) {
                                    int id = final_ctx.getResources().getIdentifier("drawable/" + "favorited", null, final_ctx.getPackageName());
                                    favorite_panel.setImageResource(id);
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

    public void loadCard(final Context ctx, final ViewGroup view_group, final String user_id) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "get_card.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            hash_map.put("date", formattedDate);
            hash_map.put("user_id", user_id);

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                Log.i("responso", response + "");
                                if (strStatus.equals("1")) {
                                    JSONObject jsonData = jsonObj.getJSONObject("data");
                                    final String serial_number = jsonData.getString("serial_number");
                                    final String expiry_date = jsonData.getString("expiry_date");
                                    /*
                                    String sn = "";
                                    if (serial_number.trim().equals("") == false) {
                                        for (int m = 0; m < serial_number.length(); m++) {
                                            if ((m + 1) % 4 == 0) {
                                                sn += serial_number.charAt(m) + " ";
                                            }
                                            else {
                                                sn += serial_number.charAt(m);
                                            }
                                        }
                                    }  */
                                    String vt = "";
                                    if (expiry_date.trim().equals("") == false){
                                        String[] sp_expiry = expiry_date.split("/");
                                        vt = sp_expiry[1] + "/" + sp_expiry[0];
                                    }

                                    ImageView ivCardFront = (ImageView) view_group.findViewById(R.id.card_front);
                                    // final RelativeLayout rlCardProgBar1 = (RelativeLayout) view_group.findViewById(R.id.card_prog_bar1);
                                    // final RelativeLayout rlCardProgBar2 = (RelativeLayout) view_group.findViewById(R.id.card_prog_bar2);
                                    TextView txtCardHolderSN = (TextView) view_group.findViewById(R.id.cardholder_serial_number);
                                    txtCardHolderSN.setText(serial_number);
                                    TextView txtCardHolderFullName = (TextView) view_group.findViewById(R.id.cardholder_fullname);
                                    txtCardHolderFullName.setText(sharedpreferences.getString(Enums.UserFirstName, "") + " " + sharedpreferences.getString(Enums.UserLastName, ""));
                                    TextView txtCardHolderVT = (TextView) view_group.findViewById(R.id.cardholder_validthrough);
                                    txtCardHolderVT.setText(vt);

                                    ImageView ivCardBack = (ImageView) view_group.findViewById(R.id.card_back);
                                    Bitmap bitmap = null;

                                    try {
                                        Log.i("qr", "1");
                                        bitmap = encodeAsBitmap(serial_number.substring(serial_number.length() - 4), BarcodeFormat.QR_CODE, 150, 150);
                                        Log.i("qr", "2");
                                        ivCardBack.setImageBitmap(bitmap);
                                        Log.i("qr", "3");

                                    } catch (WriterException e) {

                                    }
                                    /*
                                    try {
                                        Picasso.with(final_ctx).load(image_front).into(ivCardFront, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                rlCardProgBar1.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                                    } catch (Exception ex) {
                                    }
                                    try {
                                        Picasso.with(final_ctx).load(image_back).into(ivCardBack, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                rlCardProgBar2.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                                    } catch (Exception ex) {
                                    }
                                    */
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

    public void logout(final Context ctx) {

        try {
            final AlertDialog progressDialog = new SpotsDialog(ctx, R.style.Custom);
            progressDialog.show();
            final Context final_ctx = ctx;
            sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RequestQueue queue = Volley.newRequestQueue(ctx);
            final String url = Config.wsc_url + "logout.php";
            HashMap<String, String> hash_map = new HashMap<String, String>();

            hash_map.put("user_id", sharedpreferences.getLong(Enums.UserId, 0) + "");

            JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jsonObj = response;
                            try {
                                String strStatus = jsonObj.getString("status_code");
                                Log.i("responso", response + "");
                                if (strStatus.equals("1")) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
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
                                    editor.putInt(Enums.ScannerOfferSelected, 0);
                                    editor.putString(Enums.ScannerSerialNumber, "");
                                    // to be removed later
                                    // editor.putBoolean(Enums.UserLoggedIn, false);
                                    editor.commit();
                                    Intent intent = new Intent(final_ctx, HomeActivity.class);
                                    final_ctx.startActivity(intent);
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

    public void getUserFavoritesNearbyMap(final Context ctx, final ViewGroup view_group, String user_id, final String latitude, final String longitude) {

        try {
            if (Functionalities.isNetworkAvailable(ctx) == true) {
                // progressDialogGlobal = new SpotsDialog(ctx, R.style.Custom);
                // progressDialogGlobal.show();
                final Context final_ctx = ctx;
                sharedpreferences = ctx.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
                inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RequestQueue queue = Volley.newRequestQueue(ctx);
                final String url = Config.wsc_url + "get_favorites_list.php";
                HashMap<String, String> hash_map = new HashMap<String, String>();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                hash_map.put("date", formattedDate);
                hash_map.put("user_id", user_id);

                JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hash_map),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject jsonObj = response;
                                try {
                                    String strStatus = jsonObj.getString("status_code");
                                    List<String> newList = new ArrayList<String>();
                                    List<String> newListFull = new ArrayList<String>();
                                    Log.i("responso", response + "");
                                    if (strStatus.equals("1")) {
                                        JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                        int n = jsonData.length();
                                        if (n > 0) {
                                            for (int i = 0; i < n; ++i) {
                                                JSONObject jsonOffer = jsonData.getJSONObject(i);
                                                final String offer_id = jsonOffer.getString("offer_id");
                                                final String favorite_id = jsonOffer.getString("favorite_id");
                                                newList.add(offer_id);
                                                newListFull.add(offer_id + "/" + favorite_id);
                                            }
                                            final List<String> arrSelectedLocationList = newList;
                                            final List<String> arrSelectedLocationListFull = newListFull;
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(Enums.UserFavoritesList, arrSelectedLocationList.toString());
                                            editor.putString(Enums.UserOffersFavoritesList, arrSelectedLocationListFull.toString());
                                            Log.i("UserOffersFavoritesList", arrSelectedLocationListFull.toString());
                                            editor.commit();
                                        }
                                    } else {
                                        final List<String> arrSelectedLocationList = newList;
                                        final List<String> arrSelectedLocationListFull = newListFull;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Enums.UserFavoritesList, arrSelectedLocationList.toString());
                                        editor.putString(Enums.UserOffersFavoritesList, arrSelectedLocationListFull.toString());
                                        editor.commit();
                                    }
                                    displayNearbyOffersListing(ctx, latitude, longitude, view_group);

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

    public void displayNearbyOffersListing(Context ctx, String latitude, String longitude, final ViewGroup view_group){

        try {
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
                                llOfferPanelEnc = (LinearLayout) view_group.findViewById(R.id.nearbylist_panels_enc);
                                llOfferPanelEnc.removeAllViews();
                                if (strStatus.equals("1")) {
                                    JSONArray jsonData = (JSONArray) jsonObj.get("data");
                                    int n = jsonData.length();
                                    if (n > 0) {
                                        String str = sharedpreferences.getString(Enums.UserFavoritesList, "");
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
                                        final List<String> arrFavoritesList = newList;
                                        for (int i = 0; i < n; ++i) {
                                            try {
                                                ViewGroup viewOfferPanel = (ViewGroup) inflater.inflate(R.layout.activity_offer_panel, null);
                                                ViewGroup viewSeparator15 = (ViewGroup) inflater.inflate(R.layout.activity_separator_15, null);
                                                LinearLayout llHomeOfferMain = (LinearLayout) view_group.findViewById(R.id.home_deals_main);

                                                TextView txtOfferName = (TextView) viewOfferPanel.findViewById(R.id.offer_name);
                                                final ImageView btnFavorite = (ImageView) viewOfferPanel.findViewById(R.id.btn_favorite);
                                                btnFavorite.setVisibility(View.VISIBLE);
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
                                                        intent.putExtra("SELECTED_OFFER_ID", offer_id);
                                                        intent.putExtra("FROM_VIEW", "nearby_offers");
                                                        final_ctx.startActivity(intent);
                                                    }
                                                });

                                                if (arrFavoritesList.contains(offer_id) == true) {
                                                    btnFavorite.setBackgroundResource(R.drawable.deals_favorite_active1);
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // unfavorite call
                                                            getUserFavoritesId(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                } else {
                                                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            // favorite call
                                                            addToFavorites(final_ctx, btnFavorite, offer_id, "deals");
                                                        }
                                                    });
                                                }

                                                llOfferPanelEnc.addView(viewOfferPanel);
                                                llOfferPanelEnc.addView(viewSeparator15);
                                            } catch (Exception ex) {
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
                    // progressDialogGlobal.dismiss();
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

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
