package com.droidafricana.globalmail.view.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.droidafricana.globalmail.MainActivity
import com.droidafricana.globalmail.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopNewsFragmentTest {

    @get:Rule
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    @Test
    fun searchViewButtonClicked_opensSearchFragment() {
        onView(withId(R.id.fragment_search)).perform(click())
        onView(withId(R.id.searchFrag)).check(matches(isDisplayed()))
    }

    @Test
    fun searchViewButtonClicked_opensSearchFragment_returnsTextType() {
        onView(withId(R.id.fragment_search)).perform(click())
        onView(withId(R.id.search_view)).perform(click())
        onView(withId(R.id.search_src_text))
                .perform(replaceText("search"), closeSoftKeyboard())
                .check(matches(withText("search")))
    }
}