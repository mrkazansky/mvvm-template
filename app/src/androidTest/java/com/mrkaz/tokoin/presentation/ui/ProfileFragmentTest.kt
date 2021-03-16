package com.mrkaz.tokoin.presentation.ui

import android.os.SystemClock
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.mrkaz.tokoin.base.BaseUITest
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.presentation.ui.profile.ProfileFragment
import com.mrkaz.tokoin.R
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.inject
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest : BaseUITest() {

    private val authUtils: AuthUtils by inject()

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }
    }

    @Test
    fun test_login_button_tap() {
        // Arrange
        val mockNavController: NavController = mock(NavController::class.java)
        // Assign
        val scenario = launchFragmentInContainer {
            ProfileFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }


        // Assert
        onView(withId(R.id.btnLogin)).perform(ViewActions.click())
        verify(mockNavController).navigate(R.id.profile_to_login)
    }

    @Test
    fun test_register_button_tap(): Unit = runBlocking {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        SystemClock.sleep(500)
        val mockNavController: NavController = mock(NavController::class.java)
        // Assign
        val scenario = launchFragmentInContainer {
            ProfileFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }


        // Assert
        onView(withId(R.id.btnRegister)).perform(ViewActions.click())
        verify(mockNavController).navigate(R.id.profile_to_register)
    }

    @Test
    fun test_profile_auth_none_logged() {
        val mockNavController: NavController = mock(NavController::class.java)
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            ProfileFragment().also { fragment ->
                authUtils.loggingStatus.postValue(false)
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }
        SystemClock.sleep(500)
        onView(withId(R.id.txtName)).check(matches(withText("Not logged")))
        onView(withId(R.id.btnLogout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))

    }

    @Test
    fun test_profile_auth_logged() {
        val mockNavController: NavController = mock(NavController::class.java)
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            ProfileFragment().also { fragment ->
                authUtils.loggingStatus.postValue(true)
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }
        SystemClock.sleep(500)
        onView(withId(R.id.txtName)).check(matches(not(withText("Not logged"))))
        onView(withId(R.id.btnLogout)).check(matches((isDisplayed())))
        onView(withId(R.id.btnLogin)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnRegister)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_logout_button_tap(): Unit = runBlocking {
        //Arrange
        val user = UserEntity("binh", "123", null)
        UiThreadStatement.runOnUiThread {
            authUtils.logged(user)
        }
        SystemClock.sleep(500)
        val mockNavController: NavController = mock(NavController::class.java)
        // Assign
        launchFragmentInContainer {
            ProfileFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
            }
        }
        // Assert
        onView(withId(R.id.btnLogout)).perform(ViewActions.click())
        SystemClock.sleep(500)
        onView(withId(R.id.txtName)).check(matches(withText("Not logged")))
        onView(withId(R.id.btnLogout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
    }
}