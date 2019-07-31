package com.droidafricana.globalmail;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.droidafricana.globalmail.chromeCustomTabs.customTabs.CustomTabActivityHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        CustomTabActivityHelper.ConnectionCallback {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;
    private CustomTabActivityHelper mCustomTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setUpToolbar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_nav_host_fragment);

        assert navHostFragment != null;
        mNavController = navHostFragment.getNavController();

        //Hide the toolbar if we are in the search destination
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.fragment_search_dest) {
                toolbar.setVisibility(View.GONE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(getTopLevelDestinations())
                .setDrawerLayout(drawerLayout)
                .build();

        setupActionBar(mNavController);

        setupNavigationMenu(mNavController);

        //TODO: Check memory leaks here
        mCustomTabActivityHelper = new CustomTabActivityHelper();
    }

    private void setUpToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.universal_text_color));
        Objects.requireNonNull(toolbar.getOverflowIcon()).
                setColorFilter(ContextCompat.getColor(this, R.color.universal_text_color),
                        PorterDuff.Mode.SRC_ATOP);
    }

    /*Helper method for setting the top level destinations of the navigation*/
    private Set<Integer> getTopLevelDestinations() {
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.top_news_dest);
        topLevelDestinations.add(R.id.sports_news_dest);
        topLevelDestinations.add(R.id.entertainment_news_dest);
        topLevelDestinations.add(R.id.technology_news_dest);
        topLevelDestinations.add(R.id.business_news_dest);
        topLevelDestinations.add(R.id.health_news_dest);
        topLevelDestinations.add(R.id.science_news_dest);
        return topLevelDestinations;
    }

    private void setupNavigationMenu(NavController navController) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupActionBar(NavController navController) {
        //NavigationUI.setupActionBarWithNavController(this, navController, mDrawerLayout);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, mNavController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomTabActivityHelper.setConnectionCallback(null);
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    @Override
    public void onCustomTabsConnected() {
        mCustomTabActivityHelper.getSession();
    }

    @Override
    public void onCustomTabsDisconnected() {
        //Do nothing for now
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }
}
