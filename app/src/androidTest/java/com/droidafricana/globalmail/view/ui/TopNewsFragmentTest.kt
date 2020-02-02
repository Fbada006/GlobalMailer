package com.droidafricana.globalmail.view.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.droidafricana.globalmail.MainActivity
import com.droidafricana.globalmail.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TopNewsFragmentTest {

    @get:Rule
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    /*Test that the search button opens the proper fragment*/
    @Test
    fun searchViewButtonClicked_opensSearchFragment() {
        onView(withId(R.id.fragment_search)).perform(click())
        onView(withId(R.id.search_layout)).check(matches(isDisplayed()))
    }

    /*Test that the searchView returns the correct text*/
    @Test
    fun searchViewButtonClicked_opensSearchFragment_returnsTextTyped() {
        onView(withId(R.id.fragment_search)).perform(click())
        onView(withId(R.id.search_view)).perform(click())
        onView(withId(R.id.search_src_text))
                .perform(replaceText("search"), closeSoftKeyboard())
                .check(matches(withText("search")))
    }
//
//    /*Test the navigation component*/
//    @Test
//    fun clickSearchButton_navigateToSearchFragment() {
//        // GIVEN - On the top news screen
//        val scenario: FragmentScenario<TopNewsFragment> =
//                launchFragmentInContainer<TopNewsFragment>(null, R.style.AppTheme)
//       // val activityScenario = ActivityScenario.launch(MainActivity::class.java)
//
//        val navController = Mockito.mock(NavController::class.java)
//        scenario.onFragment {
//            Navigation.setViewNavController(it.view!!, navController)
//        }
////        activityScenario.onActivity {
////            Navigation.setViewNavController(it, navController)
////        }
//
//        Thread.sleep(5000)
//
//        // WHEN - Click on the search button
//       // onView(withId(R.id.fragment_search)).perform(click())
//
//        // THEN - Verify that we navigate to the search screen
////        Mockito.verify(navController).navigate(
////                TopNewsFragmentDirections.actionTopNewsDestToActivitySearchDest("general")
////        )
//    }
}