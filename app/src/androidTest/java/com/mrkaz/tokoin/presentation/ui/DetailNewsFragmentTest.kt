package com.mrkaz.tokoin.presentation.ui

import android.os.SystemClock
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.openLinkWithText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.base.BaseUITest
import com.mrkaz.tokoin.common.constant.ITEM
import com.mrkaz.tokoin.data.models.news.NewsData
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.helpers.BundleMatcher
import com.mrkaz.tokoin.presentation.ui.detail.NewsDetailFragment
import com.mrkaz.tokoin.R
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class DetailNewsFragmentTest : BaseUITest() {

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }

    }

    @Test
    fun test_data_filled_correct() {
        //Arrange
        val mockNavController: NavController = mock(NavController::class.java)
        val news = NewsData(
            "Tester",
            "Tester",
            "Adele - Don't You Remember (Live on Letterman)\n",
            "d",
            "www.google.com",
            "f",
            ""
        )
        val bundle = bundleOf(ITEM to news)
        launchFragmentInContainer<NewsDetailFragment>(
            bundle,
            themeResId = R.style.AppTheme
        ).let {
            it.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }
        SystemClock.sleep(1000)
        onView(withId(R.id.txtUrl)).check(matches(withText(news.url)))
        onView(withId(R.id.txtContent)).check(matches(isDisplayed()))
        onView(withId(R.id.txtContent)).check(matches(not(withText(""))))
    }

    @Test
    fun test_url_work() {
        //Arrange
        val mockNavController: NavController = mock(NavController::class.java)
        val news = NewsData(
            "Tester",
            "Tester",
            "Adele - Don't You Remember (Live on Letterman)\n",
            "d",
            "www.google.com",
            "f",
            ""
        )
        val bundle = bundleOf(ITEM to news)
        launchFragmentInContainer<NewsDetailFragment>(
            bundle,
            themeResId = R.style.AppTheme
        ).let {
            it.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }
        // Assert
        onView(ViewMatchers.withId(R.id.txtUrl))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.txtUrl)).perform(click())
        verify(mockNavController).navigate(
            eq(R.id.action_detail_to_web),
            argThat(BundleMatcher(bundle))
        )
    }
}