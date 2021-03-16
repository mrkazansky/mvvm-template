package com.mrkaz.tokoin.data.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mrkaz.tokoin.data.database.dao.ReferenceDAO
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class ReferenceDatabaseTest : KoinTest {

    private lateinit var referenceDAO: ReferenceDAO
    private val db: ReferenceDatabase by inject()

    @Before
    fun start() {
        referenceDAO = db.referenceDAO()
    }

    @Test
    @Throws(Exception::class)
    fun test_reference_database_insert() = runBlocking {
        //Arrange
        val references = listOf("ab", "b", "c").map { ReferenceEntity(it) }
        //Assign
        referenceDAO.insert(references)
        val actual = referenceDAO.getAll()
        //Assert
        Assert.assertTrue(actual.isNotEmpty())
        Assert.assertTrue(actual.containsAll(references))
    }

    @Test
    fun test_reference_database_get_all() = runBlocking {
        //Arrange
        val references = listOf("ab", "b", "c").map { ReferenceEntity(it) }
        //Assign
        referenceDAO.insert(references)
        val actual = referenceDAO.getAll()
        //Assert
        Assert.assertTrue(actual.isNotEmpty())
    }

}