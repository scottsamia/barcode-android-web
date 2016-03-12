package com.samiaware.android.rambo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Objects;

public class MainActivity extends Activity {

    WebView myWebView;
    Browser _myBrowser;
    Scanner _myScanner;

    private String URL = "http://run.plnkr.co/plunks/idPf98/";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);

        myWebView = (WebView) findViewById(R.id.mybrowser);
        _myBrowser = new Browser(myWebView);
        _myScanner = new Scanner(_myBrowser, this);
        _myBrowser.addJavaScriptInterface(_myScanner, "Scanner");
        _myBrowser.loadURL(URL);
        registerReceiver();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.samiaware.android.rambo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.samiaware.android.rambo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }
    private void registerReceiver()
    {
        if(_myScanner.isRegistered) return;
        //Create Intent filter to register with BroadcastReceiver on the ContextWrapper
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA);
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS);
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED);

        registerReceiver(_myScanner, filter);
        _myScanner.register();
    }
    private void unregisterReceiver()
    {
        if(!_myScanner.isRegistered) return;
        unregisterReceiver(_myScanner);
        _myScanner.unregister();
    }
    private void refresh() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        URL = sharedPref.getString(SettingsActivity.KEY_PREF_URL, "");
        _myBrowser.JavaScriptCallBack = sharedPref.getString(SettingsActivity.KEY_PREF_JAVASCRIPTCALLBACK, "");

        _myBrowser.loadURL(URL);
    }
    private boolean mIsOpened = false;
    private void destroyEvent()
    {
        if(mIsOpened)
        {
            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_BARCODE_CLOSE);
            intent.putExtra(Constants.EXTRA_HANDLE, _myScanner.barcodeHandle);
            intent.putExtra(Constants.EXTRA_INT_DATA3, 600);
            sendBroadcast(intent);
        }
        unregisterReceiver();
    }
}
