package dmax.dialog;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Back-compat shim for old SpotsDialog usage:
 *   new SpotsDialog(context, R.style.Custom)
 *
 * We extend android.app.AlertDialog so existing variables like
 *   final AlertDialog progressDialog = new SpotsDialog(...)
 * compile without touching your code.
 */
public class SpotsDialog extends AlertDialog {
    public SpotsDialog(Context context, int themeResId) {
        super(context, themeResId);
        // No-op: theme/style is applied by AlertDialog. Your code calls show()/dismiss().
    }
}
