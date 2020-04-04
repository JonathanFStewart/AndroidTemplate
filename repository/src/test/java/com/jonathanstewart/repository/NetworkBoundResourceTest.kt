package com.jonathanstewart.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NetworkBoundResourceTest {

    data class TestResultType (val name: String, val number: Int, val isOn:Boolean)

    data class TestRequestType (val requestName: String, val requestNumber: Int, val requestIsOn:Boolean)

    val testResult = TestResultType("TestName", 123, true)
    val testError = Exception("TestError")

    abstract class TestFunctions {
        abstract suspend fun saveCall (testResultType: TestResultType)
        abstract suspend fun loadCall() : TestResultType?
        abstract fun processRequest(testRequestType: TestRequestType) : TestResultType
        abstract suspend fun createAsyncCall(): TestResultType
        abstract fun shouldFetch(testResultType: TestResultType) : Boolean
    }

    suspend fun prepareNetworkBoundResource (testFunctions: TestFunctions): NetworkBoundResource<TestResultType> {
        return object :NetworkBoundResource<TestResultType> () {

            override suspend fun saveCallResults(items: TestResultType) = testFunctions.saveCall(items)

            override fun shouldFetch(data: TestResultType?): Boolean = testFunctions.shouldFetch(testResult)

            override suspend fun loadFromDb(): TestResultType? = testFunctions.loadCall()

            override suspend fun createCallAsync(): TestResultType = testFunctions.createAsyncCall()
        }.build()
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        CoroutineContextProvider.setTestContexts(true)
    }


    @Test
    fun testDBEmptyNetworkSuccessBDLoad() {
        val testFunctions : TestFunctions = mockk()
        coEvery { testFunctions.saveCall(any()) } just runs
        coEvery { testFunctions.loadCall() } returns null andThen testResult
        coEvery { testFunctions.createAsyncCall() } returns testResult
        every { testFunctions.processRequest(any()) } returns testResult
        every { testFunctions.shouldFetch(testResult) } returns true

        val testLiveData = runBlocking { prepareNetworkBoundResource(testFunctions) }.asLiveData()
        testLiveData.observeForever {  }
        assertEquals(testLiveData.value?.status, Resource.Status.SUCCESS)
        assertEquals(testLiveData.value?.data, testResult)
        assertEquals(testLiveData.value?.error, null)

        coVerify (exactly = 1) { testFunctions.saveCall(testResult) }
        coVerify (exactly = 2) { testFunctions.loadCall()}
        coVerify (exactly = 1) { testFunctions.createAsyncCall() }
    }

    @Test
    fun testNetworkFailAndNoData() {
        val testFunctions : TestFunctions = mockk()
        coEvery { testFunctions.saveCall(any()) } just runs
        coEvery { testFunctions.loadCall()} returns null
        coEvery { testFunctions.createAsyncCall() } throws testError
        every { testFunctions.processRequest(any()) } returns testResult
        every { testFunctions.shouldFetch(testResult) } returns true

        val testLiveData = runBlocking { prepareNetworkBoundResource(testFunctions) }.asLiveData()
        testLiveData.observeForever {  }
        assertEquals(testLiveData.value?.status, Resource.Status.ERROR)
        assertEquals(testLiveData.value?.data, null)
        assertEquals(testLiveData.value?.error, testError)

        coVerify (exactly = 0) { testFunctions.saveCall(testResult) }
        coVerify (exactly = 2) { testFunctions.loadCall()}
        coVerify (exactly = 1) { testFunctions.createAsyncCall() }
    }

    @Test
    fun testNetworkFailButDataInDataBase() {
        val testFunctions : TestFunctions = mockk()
        coEvery { testFunctions.saveCall(any()) } just runs
        coEvery { testFunctions.loadCall()} returns testResult
        coEvery { testFunctions.createAsyncCall() } throws testError
        every { testFunctions.processRequest(any()) } returns testResult
        every { testFunctions.shouldFetch(testResult) } returns true

        val testLiveData = runBlocking { prepareNetworkBoundResource(testFunctions) }.asLiveData()
        testLiveData.observeForever {  }
        assertEquals(testLiveData.value?.status, Resource.Status.ERROR)
        assertEquals(testLiveData.value?.data, testResult)
        assertEquals(testLiveData.value?.error, testError)

        coVerify (exactly = 0) { testFunctions.saveCall(testResult) }
        coVerify (exactly = 2) { testFunctions.loadCall()}
        coVerify (exactly = 1) { testFunctions.createAsyncCall() }
    }

    @Test
    fun testDBHasOldDataNetworkUpdatesIt() {
        var fakeDb  = TestResultType("OLDDATA", 9999, false)
        val testFunctions : TestFunctions = mockk()
        coEvery { testFunctions.saveCall(any()) } answers { fakeDb = firstArg() }
        coEvery { testFunctions.loadCall()} answers {fakeDb}
        coEvery { testFunctions.createAsyncCall() } returns testResult
        every { testFunctions.processRequest(any()) } returns testResult
        every { testFunctions.shouldFetch(testResult) } returns true

        val testLiveData = runBlocking { prepareNetworkBoundResource(testFunctions) }.asLiveData()
        testLiveData.observeForever {  }
        assertEquals(testLiveData.value?.status, Resource.Status.SUCCESS)
        assertEquals(testLiveData.value?.data, testResult)
        assertEquals(testLiveData.value?.error, null)

        coVerify (exactly = 1) { testFunctions.saveCall(testResult) }
        coVerify (exactly = 2) { testFunctions.loadCall()}
        coVerify (exactly = 1) { testFunctions.createAsyncCall() }
    }

    @Test
    fun testDoesNotFetchData() {
        val testFunctions : TestFunctions = mockk()
        coEvery { testFunctions.saveCall(any()) } just runs
        coEvery { testFunctions.loadCall()} returns testResult
        coEvery { testFunctions.createAsyncCall() } returns testResult
        every { testFunctions.processRequest(any()) } returns testResult
        every { testFunctions.shouldFetch(testResult) } returns false

        val testLiveData = runBlocking { prepareNetworkBoundResource(testFunctions) }.asLiveData()
        testLiveData.observeForever {  }
        assertEquals(testLiveData.value?.status, Resource.Status.SUCCESS)
        assertEquals(testLiveData.value?.data, testResult)
        assertEquals(testLiveData.value?.error, null)

        coVerify (exactly = 0) { testFunctions.saveCall(testResult) }
        coVerify (exactly = 1) { testFunctions.loadCall()}
        coVerify (exactly = 0) { testFunctions.createAsyncCall() }
    }

}