package com.mrkaz.tokoin.presentation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.R
import com.mrkaz.tokoin.base.BaseUITest
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.presentation.ui.main.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseUITest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }
    }

    /**
     * Running once has no problem, but running in sequence causes an error
     * Uncomment below to perform a test
     * */

    //@Test
    fun test_bottom_navigation_change_item_selection() {
        assertNewsScreen()

        openProfileScreen()
        assertProfileScreen()

        openReferenceScreen()
        assertReferenceScreen()

        openNewsScreen()
        assertNewsScreen()
    }


    private fun assertReferenceScreen() {
        onView(withId(R.id.txtNewsRef))
            .check(matches(isDisplayed()))
    }

    private fun openReferenceScreen() {
        onView(allOf(withContentDescription(R.string.title_reference), isDisplayed()))
            .perform(click())
    }

    private fun openNewsScreen() {
        onView(allOf(withContentDescription(R.string.title_news), isDisplayed()))
            .perform(click())
    }

    private fun assertNewsScreen() {
        onView(withId(R.id.txtNews))
            .check(matches(isDisplayed()))
    }

    private fun openProfileScreen() {
        onView(allOf(withContentDescription(R.string.title_profile), isDisplayed()))
            .perform(click())
    }

    private fun assertProfileScreen() {
        onView(withId(R.id.txtProfile))
            .check(matches(isDisplayed()))
    }
}