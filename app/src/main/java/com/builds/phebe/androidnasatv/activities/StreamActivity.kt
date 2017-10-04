package com.builds.phebe.androidnasatv.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.builds.phebe.androidnasatv.R
import kotlinx.android.synthetic.main.activity_stream.*

class StreamActivity : AppCompatActivity() {

    companion object {
        val EXTRA_STREAM_URL: String = "EXTRA_STREAM_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        supportActionBar?.hide()

        bindView()
    }

    private fun bindView() {
        nasaStreamVideo.setZOrderOnTop(false);
        nasaStreamVideo.setOnPreparedListener { mp ->
            mp.start()
        }
        val videoUrl: Uri = Uri.parse(intent.getStringExtra(EXTRA_STREAM_URL))
        nasaStreamVideo.setVideoURI(videoUrl)
    }
}
