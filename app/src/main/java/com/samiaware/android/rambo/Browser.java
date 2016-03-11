package com.samiaware.android.rambo;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by SamiaS on 3/11/2016.
 */
public class Browser extends WebViewClient implements Recipient {

    private WebView _browser;
    public String JavaScriptCallBack = "callFromActivity";
    private String _url = "http://run.plnkr.co/plunks/idPf98/";
    public int barcodeHandle;
    private int mCount = 0;

    Browser(WebView browser) {
        _browser = browser;
        _browser.setWebViewClient(this);
        _browser.getSettings().setJavaScriptEnabled(true);
        _browser.getSettings().setDomStorageEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            _browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
            _browser.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        _browser.setWebChromeClient(new WebChromeClient());
    }

    public void addJavaScriptInterface(Scanner object, String name ) {
        //Add JavaScript Interface
        _browser.addJavascriptInterface(object, name);
    }

    public void loadURL(String url) {
        _url = url;
        _browser.loadUrl(url);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int seq = intent.getIntExtra(Constants.EXTRA_INT_DATA3, 0);
        if (action.equals(Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA)) {
            int handle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);
            byte[] data = intent.getByteArrayExtra(Constants.EXTRA_BARCODE_DECODING_DATA);
            String result = "[BarcodeDecodingData handle : " + handle + " / count : " + mCount + " / seq : " + seq + "]\n";
            String barcode = "";
            if (data != null) barcode = new String(data);
            _browser.loadUrl("javascript:" + JavaScriptCallBack + "(\"" + barcode + "\")");
//                setResultText(result);
            mCount++;
        } else if (action.equals(Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS)) {
            barcodeHandle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);
//                setSuccessFailText("Success : "+seq);
//                if(seq == 100) mCurrentStatus = STATUS_OPEN;
//                else if(seq == 200) mCurrentStatus = STATUS_CLOSE;
//                else if(seq == 300) mCurrentStatus = STATUS_TRIGGER_ON;
//
//                refreshCurrentStatus();
            //showToast("Barcode SUCCESS:"+mBarcodeHandle);
        } else if (action.equals(Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED)) {
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
        }
    }

    @Override
    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
