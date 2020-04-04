package com.jonathanstewart.androidtemplate.test.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.Rule

abstract class ActivityTest<T : AppCompatActivity>(activityClass: Class<T>) : BaseTest() {

    @get:Rule
    val activityRule: ActivityTestRule<T> = ActivityTestRule(activityClass, true, false)

    val activity: T
        get() {
            if (activityRule.activity == null) {
                startActivity()
            }

            return activityRule.activity
        }

    fun startActivity() {
        startActivityWithIntent(Intent())
    }

    fun startActivityWithIntent(intent: Intent) {
        activityRule.launchActivity(intent)
    }

    @Throws(Throwable::class)
    fun runOnUiThread(action: () -> Unit) {
        activityRule.runOnUiThread(action)
    }

    fun waitForUiThread() {
        getInstrumentation().waitForIdleSync()
    }
}