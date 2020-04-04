package com.jonathanstewart.repository
import junit.framework.Assert.assertEquals
import org.junit.Test

class ResourceTest {

    val testData = "TESTDATA"
    val testError = Throwable("TESTERROR")
    @Test
    fun successTestWithData() {
        val testResource = Resource.success(testData)
        assertEquals(testData, testResource.data)
        assertEquals(Resource.Status.SUCCESS, testResource.status)
        assertEquals(null, testResource.error)
    }

    @Test
    fun successTestWithOutData() {
        val testResource = Resource.success(null)
        assertEquals(null, testResource.data)
        assertEquals(Resource.Status.SUCCESS, testResource.status)
        assertEquals(null, testResource.error)
    }

    @Test
    fun errorTestWithData() {
        val testResource = Resource.error(testError,testData)
        assertEquals(testData, testResource.data)
        assertEquals(Resource.Status.ERROR, testResource.status)
        assertEquals(testError, testResource.error)
    }

    @Test
    fun errorTestWithoutData() {
        val testResource = Resource.error(testError,null)
        assertEquals(null, testResource.data)
        assertEquals(Resource.Status.ERROR, testResource.status)
        assertEquals(testError, testResource.error)
    }

    @Test
    fun loadingTestWithoutData() {
        val testResource = Resource.loading(null)
        assertEquals(null, testResource.data)
        assertEquals(Resource.Status.LOADING, testResource.status)
        assertEquals(null, testResource.error)
    }

    @Test
    fun loadingTesWithData() {
        val testResource = Resource.loading(testData)
        assertEquals(testData, testResource.data)
        assertEquals(Resource.Status.LOADING, testResource.status)
        assertEquals(null, testResource.error)
    }

}