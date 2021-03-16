package com.mrkaz.tokoin.common

import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.UserEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthUtilsTest {

    private lateinit var authUtils: AuthUtils

    @Before
    fun start() {
        authUtils = AuthUtils(InstrumentationRegistry.getInstrumentation().context)
    }

    @After
    fun end() {
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        SystemClock.sleep(300)
    }

    @Test
    fun test_auth_logged() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assert
        Assert.assertEquals(authUtils.loggingStatus.value, true)
    }

    @Test
    fun test_auth_logout() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assert
        Assert.assertEquals(authUtils.loggingStatus.value, true)
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        //Assert
        Assert.assertEquals(authUtils.loggingStatus.value, false)
    }

    @Test
    fun test_auth_get_account() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assign
        val actual = authUtils.getAccount()
        //Assert
        Assert.assertNotNull(actual)
        Assert.assertEquals("binh", actual?.name)
    }

    @Test
    fun test_auth_get_account_failed() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        //Assign
        val actual = authUtils.getAccount()
        //Assert
        Assert.assertNull(actual)
    }

    @Test
    fun test_auth_get_user_data() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assign
        val actual = authUtils.getUserData()
        //Assert
        Assert.assertNotNull(actual)
        Assert.assertEquals("binh", actual?.username)
        Assert.assertEquals("123", actual?.password)
        Assert.assertEquals(null, actual?.reference)
    }

    @Test
    fun test_auth_get_user_data_failed() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        //Assign
        val actual = authUtils.getUserData()
        //Assert
        Assert.assertNull(actual)
    }

    @Test
    fun test_auth_update_account() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assign
        authUtils.updateUserdata(UserEntity("binh", "12423", "hello TOKOIN"))
        val actual = authUtils.getUserData()
        //Assert
        Assert.assertNotNull(actual)
        Assert.assertNotNull(actual)
        Assert.assertEquals("binh", actual?.username)
        Assert.assertEquals("12423", actual?.password)
        Assert.assertEquals("hello TOKOIN", actual?.reference)
    }

    @Test
    fun test_auth_update_account_failed() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        //Assign
        authUtils.updateUserdata(UserEntity("binh", "12423", "hello TOKOIN"))
        val actual = authUtils.getUserData()
        //Assert
        Assert.assertNull(actual)
    }

    @Test
    fun test_auth_peek_token_account() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logged(UserEntity("binh", "123", null))
        }
        //Assign
        val actual = authUtils.peekToken()
        //Assert
        Assert.assertNotNull(actual)
        Assert.assertEquals("binh", actual)
    }

    @Test
    fun test_auth_peek_token_failed_account_failed() {
        //Arrange
        UiThreadStatement.runOnUiThread {
            authUtils.logout()
        }
        //Assign
        val actual = authUtils.peekToken()
        //Assert
        Assert.assertNull(actual)
    }

}