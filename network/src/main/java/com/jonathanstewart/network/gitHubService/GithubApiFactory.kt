package com.jonathanstewart.network.gitHubService

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * internal object that creates the GitHub Retro fit service
 */
internal object GithubApiFactory {
    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
                .newBuilder()
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)
    }

    //OkhttpClient for building http request url
    private val flickrClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()

    /**
     * Creates an instance of retrofit for the git hub service
     * uses coroutine call adapter for async
     * and Moshi Converter factory for json unmarshalling
     */
    fun retrofit() : Retrofit = Retrofit.Builder()
            .client(flickrClient)
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    /**
     * Provides the GithubApiService
     */
    fun provideGithubApiService() : GithubApiService = retrofit().create(
        GithubApiService::class.java)
}