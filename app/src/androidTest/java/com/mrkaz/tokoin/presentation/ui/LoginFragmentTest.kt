package com.mrkaz.tokoin.presentation.ui

import android.app.Dialog
import android.os.SystemClock
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.base.BaseUITest
import com.mrkaz.tokoin.di.generateTestAppComponent
import com.mrkaz.tokoin.presentation.ui.login.LoginFragment
import com.mrkaz.tokoin.usecase.auth.AuthUseCase
import com.mrkaz.tokoin.R
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.inject
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class LoginFragmentTest : BaseUITest() {

    private val authUseCase: AuthUseCase by inject()
    private lateinit var fragmentDialog: Dialog

    @Before
    fun start() {
        super.setUp()
        startKoin {
            loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        }
        val mockNavController: NavController = Mockito.mock(NavController::class.java)
        launchFragment<LoginFragment>(themeResId = R.style.AppTheme).let {
            it.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), mockNavController)
                    }
                }
                fragment.dialog?.let { dialog ->
                    fragmentDialog = dialog
                }
            }
        }
    }

    @Test
    fun test_login_with_empty_username() {
        //Arrange
        onView(withId(R.id.tietUserName))
            .perform(typeText(""))
        onView(withId(R.id.tietPassword))
            .perform(typeText("123"))
        onView(withId(R.id.btnLogin)).perform(click())
        //Assert
        SystemClock.sleep(500)
        onView(withId(R.id.txtError)).check(matches(withText("Empty Input")))
        onView(withId(R.id.txtError)).check(matches(isDisplayed()))
    }

    @Test
    fun test_login_with_empty_password() {
        //Arrange
        onView(withId(R.id.tietUserName))
            .perform(typeText("binh"))
        onView(withId(R.id.tietPassword))
            .perform(typeText(""))
        onView(withId(R.id.btnLogin)).perform(click())
        SystemClock.sleep(500)
        //Assert
        onView(withId(R.id.txtError)).check(matches(withText("Empty Input")))
        onView(withId(R.id.txtError)).check(matches(isDisplayed()))

    }

    @Test
    fun test_login_with_wrong_password(): Unit = runBlocking {
        //Arrange
        authUseCase.register("binhh", "123")
        SystemClock.sleep(500)
        onView(withId(R.id.tietUserName))
            .perform(typeText("binhh"))
        onView(withId(R.id.tietPassword))
            .perform(typeText("1234"))
        onView(withId(R.id.btnLogin)).perform(click())
        //Assert
        SystemClock.sleep(500)
        onView(withId(R.id.txtError)).check(matches(withText("Login failed")))
        onView(withId(R.id.txtError)).check(matches(isDisplayed()))
    }

    @Test
    fun test_login_success(): Unit = runBlocking {
        //Arrange
        authUseCase.register("binhh", "123")
        SystemClock.sleep(500)
        onView(withId(R.id.tietUserName))
            .perform(typeText("binhh"))
        onView(withId(R.id.tietPassword))
            .perform(typeText("123"))
        onView(withId(R.id.btnLogin)).perform(click())
        //Assert
        Assert.assertFalse(fragmentDialog.isShowing)
    }

}