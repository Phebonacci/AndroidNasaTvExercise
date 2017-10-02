package com.builds.phebe.androidnasatv.apiclient

import com.builds.phebe.androidnasatv.apiclient.events.ApiResponseEvent
import com.builds.phebe.androidnasatv.apiclient.models.Channel
import com.builds.phebe.androidnasatv.apiclient.models.ChannelResponse
import com.builds.phebe.androidnasatv.apiclient.models.ChannelStatus
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import retrofit2.Response


/**
 * Created by tempophebe on 02/10/2017.
 */
class NasaTVConnector(private val api: NasaTVApi = NasaTVApi()) {

    fun connect() {
        val callResponse = api.getChannel()

        doAsync {
            val response: Response<ChannelResponse>?
            try {
                response = callResponse.execute()

                EventBus.getDefault().post(ApiResponseEvent(response.body()))
            } catch (e: Exception) {
                var channel = Channel()
                channel.status = ChannelStatus.OFF_AIR.toString()
                EventBus.getDefault().post(ApiResponseEvent(ChannelResponse(channel)))
            }
        }
    }
}