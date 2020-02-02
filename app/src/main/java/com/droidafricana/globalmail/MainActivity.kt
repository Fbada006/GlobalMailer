package com.droidafricana.globalmail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.droidafricana.globalmail.chromeCustomTabs.customTabs.CustomTabActivityHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CustomTabActivityHelper.ConnectionCallback {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mCustomTabActivityHelper: CustomTabActivityHelper
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = findNavController(R.id.my_nav_host_fragment)

        appBarConfiguration =
                AppBarConfiguration.Builder(
                        R.id.top_news_dest, R.id.sports_news_dest,
                        R.id.entertainment_news_dest, R.id.technology_news_dest,
                        R.id.business_news_dest, R.id.health_news_dest,
                        R.id.science_news_dest)
                        .setDrawerLayout(drawerLayout)
                        .build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        //Hide the toolbar if we are in the search destination
        //Hide the toolbar if we are in the search destination
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            if (destination.id == R.id.fragment_search_dest) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        }

        findViewById<NavigationView>(R.id.nav_view)
                .setupWithNavController(navController)

        //TODO: Check memory leaks here
        mCustomTabActivityHelper = CustomTabActivityHelper()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
                .apply {
                    setStatusBarBackground(R.color.my_background)
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return (NavigationUI.onNavDestinationSelected(item!!, navController)
                || super.onOptionsItemSelected(item))
    }

    override fun onStart() {
        super.onStart()
        mCustomTabActivityHelper.bindCustomTabsService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCustomTabActivityHelper.setConnectionCallback(null)
        mCustomTabActivityHelper.unbindCustomTabsService(this)
    }

    override fun onCustomTabsDisconnected() {
        mCustomTabActivityHelper.session
    }

    override fun onCustomTabsConnected() {
        //Do nothing
    }
}