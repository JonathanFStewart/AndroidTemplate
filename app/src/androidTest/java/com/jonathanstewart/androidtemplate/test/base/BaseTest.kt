package com.jonathanstewart.androidtemplate.test.base

import android.os.RemoteException
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.koin.test.KoinTest

abstract class BaseTest : KoinTest {

    @Before
    @CallSuper
    open fun setUp() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        try {
            if (!device.isScreenOn) {
                device.wakeUp()
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}