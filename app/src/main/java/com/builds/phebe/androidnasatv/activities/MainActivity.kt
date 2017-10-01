package com.builds.phebe.androidnasatv.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.builds.phebe.androidnasatv.R
import com.builds.phebe.androidnasatv.apiclient.NasaTVApi
import com.builds.phebe.androidnasatv.apiclient.models.Channel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val api: NasaTVApi = NasaTVApi()

    private var channel: Channel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        bindViews()
    }

    private fun bindViews() {
        setBackground()
        initChannel()
        setClickListener()
    }

    private fun setBackground() {
        backgroundVideo.setZOrderOnTop(false)
        backgroundVideo.setOnPreparedListener{ mp ->
            mp.isLooping = true;
            mp.start()
        }
        val backgroundUri : Uri = Uri.parse("android:resource://${packageName}/$(R.raw.intro_bg")
        backgroundVideo.setVideoURI(backgroundUri)
    }

    private fun initChannel() {
        TODO("implement channel initialization")
    }

    private fun setClickListener() {
        backgroundVideo.onClick {
            if (channel?.status == "live") {
                TODO("implement video activity")
            } else {
                toast (R.string.channel_not_ready)
            }
        }
    }
}
