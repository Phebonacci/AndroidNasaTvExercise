package com.builds.phebe.androidnasatv.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.builds.phebe.androidnasatv.R
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        initialize()
    }

    private fun initialize() {
        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 3000)
    }
}
