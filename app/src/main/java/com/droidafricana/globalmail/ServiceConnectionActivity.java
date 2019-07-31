package com.droidafricana.globalmail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.droidafricana.globalmail.chromeCustomTabs.customTabs.CustomTabActivityHelper;

/**
 * This Activity connect to the Chrome Custom Tabs Service on startup, and allows you to decide
 * when to call mayLaunchUrl.
 */
public class ServiceConnectionActivity extends AppCompatActivity
        implements CustomTabActivityHelper.ConnectionCallback {
    private CustomTabActivityHelper customTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    @Override
    public void onCustomTabsConnected() {

    }

    @Override
    public void onCustomTabsDisconnected() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
    }

}
