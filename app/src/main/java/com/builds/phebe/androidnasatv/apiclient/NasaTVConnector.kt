package com.builds.phebe.androidnasatv.apiclient

import android.os.Handler
import com.builds.phebe.androidnasatv.apiclient.events.ChannelConnectedEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync


/**
 * Created by tempophebe on 02/10/2017.
 */
class NasaTVConnector(private val api: NasaTVApi = NasaTVApi()) {

    fun connect() {
        val callResponse = api.getChannel()

        doAsync {
            val response = callResponse.execute()
            EventBus.builder().build().post(ChannelConnectedEvent(response.body()))
        }
    }
}