package com.builds.phebe.androidnasatv.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.builds.phebe.androidnasatv.R
import com.builds.phebe.androidnasatv.apiclient.NasaTVApi
import com.builds.phebe.androidnasatv.apiclient.events.ChannelConnectedEvent
import com.builds.phebe.androidnasatv.apiclient.models.Channel
import com.builds.phebe.androidnasatv.apiclient.models.ChannelStatus
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val api: NasaTVApi = NasaTVApi()

    private var channel: Channel? = Channel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        bindViews()
    }

    private fun bindViews() {
        setBackground()
        establishChannelConnection()
        setClickListener()
    }

    private fun setBackground() {
        backgroundVideo.setZOrderOnTop(false)
        backgroundVideo.setOnPreparedListener{ mp ->
            mp.isLooping = true
            mp.start()
        }
        val backgroundUri : Uri = Uri.parse("android:resource://${packageName}/$(R.raw.intro_bg")
        backgroundVideo.setVideoURI(backgroundUri)
    }

    private fun establishChannelConnection() {
        showProgress(true)
        channel?.status = ChannelStatus.CONNECTING.toString()
        api.getChannel().execute()
    }

    private fun setClickListener() {
        backgroundVideo.onClick {
            when(channel?.status) {
                ChannelStatus.CONNECTING.toString() -> {
                    toast (R.string.channel_not_ready)
                }
                ChannelStatus.LIVE.toString() -> {
                    TODO("implement video activity")
                }
                ChannelStatus.OFF_AIR.toString() -> {
                    establishChannelConnection()
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            connectionProgress.visibility = View.VISIBLE
            connectionProgressText.visibility = View.VISIBLE
        } else {
            connectionProgress.visibility = View.INVISIBLE
            connectionProgressText.visibility = View.INVISIBLE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onConnectionEstablished(event: ChannelConnectedEvent) {
        if (event.response == null || event.response?.channel == null) toast(R.string.unable_to_connect)

        TODO("complete implementation of onConnectionEstablished")

        showProgress(false)
    }
}
