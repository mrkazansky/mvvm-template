package com.mrkaz.tokoin.presentation.ui

import android.os.SystemClock
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.helpers.recyclerItemAtPosition
import com.mrkaz.tokoin.helpers.withCustomConstraints
import com.mrkaz.tokoin.presentation.ui.news.NewsAdapter
import com.mrkaz.tokoin.presentation.ui.news.NewsFragment
import com.mrkaz.tokoin.R
import com.mrkaz.tokoin.base.BaseUITest
import com.google.gson.Gson
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
class NewsFragmentTest : BaseUITest() {

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }
        launchFragmentInContainer<NewsFragment>(themeResId = R.style.AppTheme)
    }

    @Test
    fun test_recyclerview_news_for_expected_response() {
        //Arrange
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        //Assign
        val sampleResponse = getJson("success_news_list.json")
        val expectedResponse = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        SystemClock.sleep(500)
        //Check error view holder is invisible
        onView(withId(R.id.viewHolder)).check(matches(not(isDisplayed())))
        //Check loading progress is invisible
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        //Assert first item in rcv list
        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        hasDescendant(withText(expectedResponse.articles[0].author))
                    )
                )
            )

        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        hasDescendant(withText(expectedResponse.articles[0].description))
                    )
                )
            )

        //Scroll to some index in json
        onView(withId(R.id.rcvItems)).perform(
            RecyclerViewActions.scrollToPosition<NewsAdapter.NewsHolder>(10)
        )
        //Check if item at 9th position is having 9th element in json
        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        10,
                        hasDescendant(withText(expectedResponse.articles[10].author))
                    )
                )
            )

        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        10,
                        hasDescendant(withText(expectedResponse.articles[10].description))
                    )
                )
            )
    }

    @Test
    fun test_error_view_for_error_response() {
        //Arrange
        mockNetworkResponseWithFileContent(
            "success_news_list.json",
            HttpURLConnection.HTTP_CONFLICT
        )
        SystemClock.sleep(500)
        //Assert
        onView(withId(R.id.txtError)).check(matches(withText("HTTP 409 Client Error")))
        onView(withId(R.id.viewHolder)).check(matches(isDisplayed()))
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_recyclerview_news_with_swipe_refresh_for_expected_response() {
        //Arrange
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        SystemClock.sleep(500)
        //Assign
        val sampleResponse = getJson("success_news_list.json")
        val expectedResponse = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        onView(withId(R.id.srlItems)).perform(
            withCustomConstraints(
                swipeDown(),
                isDisplayingAtLeast(85)
            )
        )
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        SystemClock.sleep(500)
        //Check error view holder is invisible
        onView(withId(R.id.viewHolder)).check(matches(not(isDisplayed())))
        //Check loading progress is invisible
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        //Assert first item in rcv list
        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        hasDescendant(withText(expectedResponse.articles[0].author))
                    )
                )
            )

        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        hasDescendant(withText(expectedResponse.articles[0].description))
                    )
                )
            )

    }
}