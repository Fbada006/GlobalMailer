package com.droidafricana.globalmail

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {

    @Test
    fun drawerNavigationFromTopNewsToFavourites() {
        // start up MainActivity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start sports screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.sports_news_dest))

        // Check that sports screen was opened.
        onView(withId(R.id.sports_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start ent screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.entertainment_news_dest))

        // Check that ent screen was opened.
        onView(withId(R.id.ent_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start tech screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.technology_news_dest))

        // Check that tech screen was opened.
        onView(withId(R.id.tech_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start business screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.business_news_dest))

        // Check that business screen was opened.
        onView(withId(R.id.bus_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start health screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.health_news_dest))

        // Check that health screen was opened.
        onView(withId(R.id.health_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start science screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.science_news_dest))

        // Check that science screen was opened.
        onView(withId(R.id.science_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start favs screen.
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.fav_news_dest))

        // Check that favs screen was opened.
        onView(withId(R.id.fav_layout)).check(matches(isDisplayed()))

        //The drawer is closed
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()) // Open Drawer

        // Start Top News screen
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.top_news_dest))

        // Check that Top News screen was opened.
        onView(withId(R.id.general_layout)).check(matches(isDisplayed()))

        // When using ActivityScenario.launch, always call close()
        activityScenario.close()
    }
}