package com.jonathanstewart.androidtemplate.test.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonathanstewart.androidtemplate.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

}