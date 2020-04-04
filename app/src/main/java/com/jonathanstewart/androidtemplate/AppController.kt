package com.jonathanstewart.androidtemplate

import android.app.Application
import com.jonathanstewart.di.gitRepoSearchModule
import com.jonathanstewart.network.di.networkModule
import com.jonathanstewart.persistence.di.gitRepoSearchPersistenceModule
import com.jonathanstewart.repository.di.githubRepoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppController : Application() {

    override fun onCreate(){
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@AppController)
            // modules
            modules(listOf(
                githubRepoModule,
                gitRepoSearchModule,
                networkModule,
                gitRepoSearchPersistenceModule

            ))
        }
    }
}