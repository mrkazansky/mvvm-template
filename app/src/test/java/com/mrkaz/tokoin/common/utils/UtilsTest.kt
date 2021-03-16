package com.mrkaz.tokoin.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.common.utils.Utils
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class UtilsTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var utils : Utils

    @Before
    fun start() {
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
        utils = Utils(mockk(relaxed = true))
    }

    @Test
    fun test_utils_md5(){
        //Assign
        val actual = utils.md5("123")
        //Assign
        assertTrue(actual.isNotBlank())
    }

}