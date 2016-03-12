package com.samiaware.android.rambo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by SamiaS on 3/11/2016.
 */
public class Scanner extends BroadcastReceiver implements JavaScript {

    public boolean isRegistered;
    public int barcodeHandle;
    private int mCount = 0;
    private BarcodeRecipient _recipient;
    private Context _context;

    Scanner(BarcodeRecipient recipient, Context context) {
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
        String action = intent.getAction();

        switch (action) {
            case Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA: {
                Resources res = _context.getResources();
                String[] symbology = res.getStringArray(R.array.symbology);
                String barcodeType = "";
                String value = intent.getStringExtra(Constants.EXTRA_STR_DATA1);
                int parameter = intent.getIntExtra(Constants.EXTRA_INT_DATA2, 0);
                int seq = intent.getIntExtra(Constants.EXTRA_INT_DATA3, 0);

                barcodeHandle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);
                byte[] data = intent.getByteArrayExtra(Constants.EXTRA_BARCODE_DECODING_DATA);
                //String result = "[BarcodeDecodingData handle : " + barcodeHandle + " / count : " + mCount + " / seq : " + seq + "]";
                String barcode = "";
                if (data != null) {
                    barcode = new String(data);
                    if (parameter > 0 && parameter < 143) { barcodeType = symbology[parameter].toString();}
                }
                _recipient.onReceive(barcode, barcodeType);
                mCount++;

                break;
            }
            case Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS:
                barcodeHandle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);

                break;
            case Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED: {
                int result = intent.getIntExtra(Constants.EXTRA_INT_DATA2, 0);
                if (result == Constants.ERROR_BARCODE_DECODING_TIMEOUT) {
//                    setSuccessFailText("Failed result : "+"Decode Timeout"+" / seq : "+seq);
                } else if (result == Constants.ERROR_NOT_SUPPORTED) {
//                    setSuccessFailText("Failed result : "+"Not Supoorted"+" / seq : "+seq);
                } else if (result == Constants.ERROR_BARCODE_ERROR_USE_TIMEOUT) {
//                    mCurrentStatus = STATUS_CLOSE;
//                    setSuccessFailText("Failed result : "+"Use Timeout"+" / seq : "+seq);
                } else if (result == Constants.ERROR_BARCODE_ERROR_ALREADY_OPENED) {
                    //mCurrentStatus = STATUS_CLOSE;
//                    setSuccessFailText("Failed result : "+"Already opened"+" / seq : "+seq);
                } else {
//                    setSuccessFailText("Failed result : "+result+" / seq : "+seq);
                }
                //showToast("Barcode FAILED:"+result);
//                refreshCurrentStatus();
                break;
            }
        }

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
