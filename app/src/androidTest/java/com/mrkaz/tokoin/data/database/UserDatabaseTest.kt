package com.mrkaz.tokoin.data.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.data.database.dao.UserDAO
import com.mrkaz.tokoin.data.database.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : KoinTest {

    private lateinit var userDao: UserDAO
    private val db: UserDatabase by inject()

    @Before
    fun start() {
        userDao = db.userDAO()
    }

    @Test
    @Throws(Exception::class)
    fun test_user_database_insert() = runBlocking {
        //Arrange
        val username = "test ${Random.nextInt(0, 10000)}"
        val user = UserEntity(username, "123", "bitcoin")
        //Assign
        userDao.insert(user)
        val actual = userDao.login(username, "123")
        //Assert
        Assert.assertTrue(actual.isNotEmpty())
        Assert.assertEquals(username, actual[0].username)
        Assert.assertEquals("123", actual[0].password)
    }

    @Test
    fun test_user_database_update() = runBlocking {
        //Arrange
        val username = "test ${Random.nextInt(0, 10000)}"
        val user = UserEntity(username, "123", "bitcoin")
        userDao.insert(user)
        user.reference = "bitcoin"
        userDao.update(user)
        val actual = userDao.login(username, "123")
        //Assert
        Assert.assertTrue(actual.isNotEmpty())
        Assert.assertEquals(username, actual[0].username)
        Assert.assertEquals("bitcoin", actual[0].reference)
    }

    @Test
    fun test_user_database_login() = runBlocking {
        val username = "test ${Random.nextInt(0, 10000)}"
        val user = UserEntity(username, "123", "bitcoin")
        userDao.insert(user)
        user.reference = "bitcoin"
        userDao.update(user)
        val actual = userDao.login(username, "123")
        Assert.assertTrue(actual.isNotEmpty())
        Assert.assertEquals("bitcoin", actual[0].reference)
    }
}