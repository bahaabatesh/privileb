package com.eventus.privileb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventus.privileb.Util.WSC;

/**
 * Created by brahim_63 on 6/4/2017.
 */

public class ImagePopupActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_popup);
        final WSC _wsc = new WSC();

        TextView btnCancel = (TextView) findViewById(R.id.image_popup_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView imGallery = (ImageView) findViewById(R.id.gallery_image);

        if (getIntent().getStringExtra("SELECTED_GALLERY") != null) {
            if (getIntent().getStringExtra("SELECTED_GALLERY") != "") {
                String sGallery = getIntent().getStringExtra("SELECTED_GALLERY");
                new WSC.DownloadImageTask(imGallery, this)
                        .execute(sGallery);
            }
        }


    }

}
