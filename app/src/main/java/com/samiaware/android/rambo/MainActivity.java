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

    WebView myBrowser;
    private boolean mIsRegisterReceiver;
    private String JavaScriptCallBack = "callFromActivity";
    private String URI = "http://run.plnkr.co/plunks/idPf98/";
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

        initialize();
        registerReceiver();
        myBrowser = (WebView) findViewById(R.id.mybrowser);

        myBrowser.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        //Add JavaScript Interface
        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
        myBrowser.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
        myBrowser.getSettings().setJavaScriptEnabled(true);
        myBrowser.getSettings().setDomStorageEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            myBrowser.getSettings().setAllowUniversalAccessFromFileURLs(true);
            myBrowser.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        myBrowser.setWebChromeClient(new WebChromeClient());
        //myBrowser.loadUrl("http://run.plnkr.co/plunks/idPf98/");
        loadURL();


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
    private void refresh() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        URI = sharedPref.getString(SettingsActivity.KEY_PREF_URI, "");
        JavaScriptCallBack = sharedPref.getString(SettingsActivity.KEY_PREF_JAVASCRIPTCALLBACK, "");

        loadURL();
    }
    private void loadURL() {

            myBrowser.loadUrl(URI);

    }
    private void initialize()
    {

        mIsRegisterReceiver = false;

    }
    private void registerReceiver()
    {
        if(mIsRegisterReceiver) return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA);
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS);
        filter.addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED);

        registerReceiver(mReceiver, filter);
        mIsRegisterReceiver = true;
    }
    private void unregisterReceiver()
    {
        if(!mIsRegisterReceiver) return;
        unregisterReceiver(mReceiver);
        mIsRegisterReceiver = false;
    }
    private int mBarcodeHandle;
    private int mCount = 0;

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int seq = intent.getIntExtra(Constants.EXTRA_INT_DATA3, 0);
            if(action.equals(Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA))
            {
                int handle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);
                byte[] data = intent.getByteArrayExtra(Constants.EXTRA_BARCODE_DECODING_DATA);
                String result = "[BarcodeDecodingData handle : "+handle+" / count : "+mCount+" / seq : "+seq+"]\n";
                String barcode = "";
                if(data!=null) barcode = new String(data);
                    myBrowser.loadUrl("javascript:" + JavaScriptCallBack + "(\"" + barcode + "\")");
//                setResultText(result);
                mCount++;
            }
            else if(action.equals(Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS))
            {
                mBarcodeHandle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0);
//                setSuccessFailText("Success : "+seq);
//                if(seq == 100) mCurrentStatus = STATUS_OPEN;
//                else if(seq == 200) mCurrentStatus = STATUS_CLOSE;
//                else if(seq == 300) mCurrentStatus = STATUS_TRIGGER_ON;
//
//                refreshCurrentStatus();
                //showToast("Barcode SUCCESS:"+mBarcodeHandle);
            }
            else if(action.equals(Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED))
            {
                int result = intent.getIntExtra(Constants.EXTRA_INT_DATA2, 0);
                if(result == Constants.ERROR_BARCODE_DECODING_TIMEOUT)
                {
//                    setSuccessFailText("Failed result : "+"Decode Timeout"+" / seq : "+seq);
                }
                else if(result == Constants.ERROR_NOT_SUPPORTED)
                {
//                    setSuccessFailText("Failed result : "+"Not Supoorted"+" / seq : "+seq);
                }
                else if(result == Constants.ERROR_BARCODE_ERROR_USE_TIMEOUT)
                {
//                    mCurrentStatus = STATUS_CLOSE;
//                    setSuccessFailText("Failed result : "+"Use Timeout"+" / seq : "+seq);
                }
                else if(result == Constants.ERROR_BARCODE_ERROR_ALREADY_OPENED)
                {
                    //mCurrentStatus = STATUS_CLOSE;
//                    setSuccessFailText("Failed result : "+"Already opened"+" / seq : "+seq);
                }
                else {
//                    setSuccessFailText("Failed result : "+result+" / seq : "+seq);
                }
                //showToast("Barcode FAILED:"+result);
//                refreshCurrentStatus();
            }
        }
    };

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
        destroyEvent();
        super.onDestroy();
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(MainActivity.this);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }

    }

    private boolean mIsOpened = false;
    private void destroyEvent()
    {
        if(mIsOpened)
        {
            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_BARCODE_CLOSE);
            intent.putExtra(Constants.EXTRA_HANDLE, mBarcodeHandle);
            intent.putExtra(Constants.EXTRA_INT_DATA3, 600);
            sendBroadcast(intent);
        }
        unregisterReceiver();
    }
}
