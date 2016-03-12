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
public class Browser extends WebViewClient implements BarcodeRecipient {

    private WebView _browser;
    public String JavaScriptCallBack = "callFromActivity";
    private String _url = "http://run.plnkr.co/plunks/idPf98/";

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

    public void onReceive(String barcode, String barcodeType) {
        _browser.loadUrl("javascript:" + JavaScriptCallBack + "(\"" + barcode + "\",\"" + barcodeType + "\")");
    }

    @Override
    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
