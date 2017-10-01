package com.builds.phebe.androidnasatv.apiclient.services

import com.builds.phebe.androidnasatv.apiclient.models.ChannelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Penguin on 01/10/2017.
 */
interface ChannelService {

    @GET("channels/{channelId}.json")
    fun getChannels(@Path("channelId") channelId: String) : Call<ChannelResponse>
}