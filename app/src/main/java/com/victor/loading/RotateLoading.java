package com.victor.loading;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.eventus.privileb.R;

/**
 * Replacement for the old Victor RotateLoading.
 * Supports optional attrs: loading_color, loading_width (width is ignored for native spinner).
 */
public class RotateLoading extends FrameLayout {
    private ProgressBar progress;
    private boolean started = false;

    public RotateLoading(Context context) {
        this(context, null);
    }

    public RotateLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Large indeterminate spinner (built-in)
        progress = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        progress.setIndeterminate(true);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateLoading);

            // Optional color tint for the spinner
            if (a.hasValue(R.styleable.RotateLoading_loading_color)) {
                int color = a.getColor(R.styleable.RotateLoading_loading_color, 0);
                if (color != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        progress.setIndeterminateTintList(ColorStateList.valueOf(color));
                    }
                }
            }

            // loading_width is accepted but not used by the native spinner
            // (kept to satisfy AAR layouts that set it)
            a.recycle();
        }

        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER;
        addView(progress, lp);

        progress.setVisibility(GONE);
    }

    public void start() {
        started = true;
        progress.setVisibility(VISIBLE);
    }

    public void stop() {
        started = false;
        progress.setVisibility(GONE);
    }

    public boolean isStart() {
        return started;
    }
}
