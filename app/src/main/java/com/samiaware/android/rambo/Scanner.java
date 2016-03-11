package com.samiaware.android.rambo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by SamiaS on 3/11/2016.
 */
public class Scanner extends BroadcastReceiver implements JavaScript {

    public boolean isRegistered;
    private Recipient _recipient;
    private Context _context;

    Scanner(Recipient recipient, Context context) {
        isRegistered = false;
        _recipient = recipient;
        _context = context;
    }

    public void register() {
        if (isRegistered) return;
        isRegistered = true;
    }

    public void unregister() {
        if (!isRegistered) return;
        isRegistered = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        _recipient.onReceive(context, intent);
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(_context, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public void openAndroidDialog(String title, String message, String posButtonMsg) {
        AlertDialog.Builder myDialog
                = new AlertDialog.Builder(_context);
        myDialog.setTitle(title);
        myDialog.setMessage(message);
        myDialog.setPositiveButton(posButtonMsg, null);
        myDialog.show();
    }

}
