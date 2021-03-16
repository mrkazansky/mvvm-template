package com.mrkaz.tokoin.presentation.ui

import android.os.SystemClock
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.mrkaz.tokoin.base.BaseUITest
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.UserDatabase
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.helpers.recyclerItemAtPosition
import com.mrkaz.tokoin.helpers.withCustomConstraints
import com.mrkaz.tokoin.presentation.ui.news.NewsAdapter
import com.mrkaz.tokoin.presentation.ui.newsReference.NewsReferenceFragment
import com.mrkaz.tokoin.R
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class NewsRefFragmentTest : BaseUITest() {

    private val referenceDatabase: ReferenceDatabase by inject()
    private val userDatabase: UserDatabase by inject()
    private val authUtils: AuthUtils by inject()
    private val referencesList = listOf("bitcoin", "animal", "apple").map { ReferenceEntity(it) }

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }
        launchFragmentInContainer<NewsReferenceFragment>(themeResId = R.style.AppTheme)
    }

    @Test
    fun test_recyclerview_news_ref_for_expected_response(): Unit = runBlocking {
        //Arrange
        referenceDatabase.referenceDAO().insert(referencesList)
        SystemClock.sleep(500)
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
    fun test_news_ref_error_view_for_error_response(): Unit = runBlocking {
        //Arrange
        referenceDatabase.referenceDAO().insert(referencesList)
        SystemClock.sleep(500)
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
    fun test_recyclerview_news_ref_with_swipe_refresh_for_expected_response() {
        //Arrange
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        //Assign
        val sampleResponse = getJson("success_news_list.json")
        val expectedResponse = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        onView(withId(R.id.srlItems)).perform(
            withCustomConstraints(
                swipeDown(),
                isDisplayingAtLeast(85)
            )
        )
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

    @Test
    fun test_spinner_filled_data_correct(): Unit = runBlocking {
        //Arrange
        referenceDatabase.referenceDAO().insert(referencesList)
        SystemClock.sleep(500)
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        //Assert
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(referencesList[1].reference.toUpperCase()))))
    }

    @Test
    fun test_spinner_change_refresh_news(): Unit = runBlocking {
        //Arrange
        referenceDatabase.referenceDAO().insert(referencesList)
        SystemClock.sleep(500)
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        //Assign
        val sampleResponse = getJson("success_news_list.json")
        val expectedResponse = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        SystemClock.sleep(500)
        //Assert
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
        //Arrange
        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        //Assert
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(referencesList[1].reference.toUpperCase()))))
        //Arrange
        mockNetworkResponseWithFileContent("success_reference_list.json", HttpURLConnection.HTTP_OK)
        //Assign
        val sampleReferenceResponse = getJson("success_reference_list.json")
        val expectedReferenceResponse =
            Gson().fromJson(sampleReferenceResponse, NewsResponse::class.java)
        SystemClock.sleep(500)
        //Assert
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
                        hasDescendant(withText(expectedReferenceResponse.articles[0].author))
                    )
                )
            )

        onView(withId(R.id.rcvItems))
            .check(
                matches(
                    recyclerItemAtPosition(
                        0,
                        hasDescendant(withText(expectedReferenceResponse.articles[0].description))
                    )
                )
            )

    }

    @Test
    fun test_spinner_change_update_user_data(): Unit = runBlocking {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        val username = "test ${Random.nextInt(0, 10000)}"
        val user = UserEntity(username, "1234", null)
        referenceDatabase.referenceDAO().insert(referencesList)
        userDatabase.userDAO().insert(user)
        UiThreadStatement.runOnUiThread {
            authUtils.logged(user)
        }
        SystemClock.sleep(1000)
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)

        onView(withId(R.id.spinner)).perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(referencesList[1].reference.toUpperCase()))))
        SystemClock.sleep(500)
        val actual = userDatabase.userDAO().login(user.username, user.password)[0].reference
        val currentUserActual = authUtils.getUserData()?.reference
        Assert.assertEquals(referencesList[1].reference, actual)
        Assert.assertEquals(referencesList[1].reference, currentUserActual)
    }


}