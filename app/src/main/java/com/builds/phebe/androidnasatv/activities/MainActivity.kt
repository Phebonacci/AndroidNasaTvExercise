package com.builds.phebe.androidnasatv.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.builds.phebe.androidnasatv.R
import com.builds.phebe.androidnasatv.apiclient.NasaTVConnector
import com.builds.phebe.androidnasatv.apiclient.events.ApiResponseEvent
import com.builds.phebe.androidnasatv.apiclient.models.Channel
import com.builds.phebe.androidnasatv.apiclient.models.ChannelStatus
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    // region variables
    companion object {
        val UPDATE_CONNECTION_STATUS_DELAY = 2000L
        val LOADING_MESSAGES_SIZE = 4
    }

    private val channelConnector: NasaTVConnector = NasaTVConnector()

    private var channel: Channel? = Channel()

    private val loadingStatusHandler: Handler = Handler()
    private val loadingStatusTask: Runnable
    private var loadingIterations = 0

    init {
        loadingStatusTask = Runnable {
            setLoadingStatus()
        }
    }
    // endregion

    // region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        bindViews()
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
        loadingStatusHandler.removeCallbacks(loadingStatusTask)
    }
    // endregion

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
        val backgroundUri : Uri = Uri.parse("android.resource://${packageName}/${R.raw.intro_bg}")
        backgroundVideo.setVideoURI(backgroundUri)
    }

    private fun establishChannelConnection() {
        showProgress(true)
        setStatus(ChannelStatus.CONNECTING)
        channel?.status = ChannelStatus.CONNECTING.toString()
        loadingStatusHandler.postDelayed(loadingStatusTask, 0L)
        Handler().postDelayed({
            channelConnector.connect()
        }, 3000L)
    }

    private fun setClickListener() {
        backgroundView.onClick {
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

    private fun setStatus(status: ChannelStatus) {
        val statusTextResource: Int
        val statusColor: Int
        when(status) {
            ChannelStatus.CONNECTING -> {
                statusTextResource = R.string.status_connecting
                statusColor = Color.YELLOW
            }
            ChannelStatus.LIVE -> {
                statusTextResource = R.string.status_live
                statusColor = Color.GREEN
                connectionProgressText.text = getString(R.string.ready)
            }
            ChannelStatus.OFF_AIR -> {
                statusTextResource = R.string.status_off_air
                statusColor = Color.RED
                connectionProgressText.text = getString(R.string.off_air)
            }
        }

        statusText.text = resources.getString(statusTextResource)
        statusText.compoundDrawables.forEach({drawable: Drawable? ->
            drawable?.colorFilter = PorterDuffColorFilter(statusColor, PorterDuff.Mode.SRC_IN)
        })
    }

    private fun setLoadingStatus() {
        val validIndex: Int =
                if (loadingIterations < LOADING_MESSAGES_SIZE) loadingIterations
                else loadingIterations % LOADING_MESSAGES_SIZE

        connectionProgressText.text = resources.getString(getResourceIdFromName("loading$validIndex"))
        loadingStatusHandler.postDelayed(loadingStatusTask, UPDATE_CONNECTION_STATUS_DELAY)
        loadingIterations++
    }

    private fun getResourceIdFromName(name: String) : Int {
        return resources.getIdentifier(name, "string", packageName)
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            connectionProgress.visibility = View.VISIBLE
        } else {
            connectionProgress.visibility = View.INVISIBLE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onConnectionEstablished(event: ApiResponseEvent) {
        loadingStatusHandler.removeCallbacks(loadingStatusTask)

        channel = event.response?.channel
        when(channel?.status) {
            ChannelStatus.LIVE.toString() -> {
                setStatus(ChannelStatus.LIVE)
            }
            else -> {
                setStatus(ChannelStatus.OFF_AIR)
            }
        }

        showProgress(false)
    }
}
