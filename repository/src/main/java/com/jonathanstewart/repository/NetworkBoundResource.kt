package com.jonathanstewart.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.coroutineContext

/**
 * Abstract class which handles returning data from the network, storing the result in the DB and then
 * providing the result from the DB. This is so that we maintain the single source
 * of truth principle by ensuring our data is always coming from the Local DB
 *
 */

abstract class NetworkBoundResource<ResultType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    /**
     * Attempts to load data from the database and determines if a network request is required
     * if so the network request is made and any exceptions are wrapped in the Resource object
     * the results are then stored in the DB and reloaded from the DB and provided to the caller
     * through the Live Data produced
     */
    suspend fun build(): NetworkBoundResource<ResultType> {
        withContext(Dispatchers.Main) { result.value =
            Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork(dbResult)
                } catch (e: Exception) {
                    Timber.e(e, "com.jonathanstewart.repository.NetworkBoundResource An error happened: $e")
                    setValue(Resource.error(e, loadFromDb()))
                }
            } else {
                Timber.d(NetworkBoundResource::class.java.name, "Return data from local database")
                setValue(Resource.success(dbResult))
            }
        }
        return this
    }

    /**
     * provides the result as a live data object as the operation are preformed asynchronously
     */
    fun asLiveData() = result as LiveData<Resource<ResultType>>

    /**
     * Fetches the Data from the network
     */
    private suspend fun fetchFromNetwork(dbResult: ResultType?) {
        Timber.d(NetworkBoundResource::class.java.name, "Fetch data from network")
        setValue(Resource.loading(dbResult))
        val apiResponse = createCallAsync()
        Timber.e(NetworkBoundResource::class.java.name, "Data fetched from network")
        saveCallResults(apiResponse)
        setValue(Resource.success(loadFromDb()))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Timber.d(NetworkBoundResource::class.java.name, "Resource: $newValue")
        if (result.value != newValue) result.postValue(newValue)
    }

    /**
     * Method that is overridden and handles saving the results to the DB
     */
    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)
    /**
     * Method that is overridden and is used to determine if the Data in DB should be refreshed from the network
     */
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean
    /**
     * Method that is overridden and handles loading the results from the DB
     */
    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType?
    /**
     * Method that is overridden and handled making the network request for the data
     */
    @MainThread
    protected abstract suspend fun createCallAsync(): ResultType
}