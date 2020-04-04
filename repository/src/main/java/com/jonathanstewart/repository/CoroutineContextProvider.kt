package com.jonathanstewart.repository

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object CoroutineContextProvider {
    var Main: CoroutineContext = Dispatchers.Main
    var IO: CoroutineContext = Dispatchers.IO
    var Default: CoroutineContext = Dispatchers.Default

    fun setTestContexts(isTesting: Boolean = true) {
        if (isTesting) {
            Main = Dispatchers.Unconfined
            IO = Dispatchers.Unconfined
            Default = Dispatchers.Unconfined
        } else {
            Main = Dispatchers.Main
            IO = Dispatchers.IO
            Default = Dispatchers.Default
        }
    }
}