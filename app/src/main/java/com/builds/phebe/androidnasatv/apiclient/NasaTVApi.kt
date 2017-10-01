package com.builds.phebe.androidnasatv.apiclient

import com.builds.phebe.androidnasatv.apiclient.models.ChannelResponse
import com.builds.phebe.androidnasatv.apiclient.services.ChannelService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Penguin on 01/10/2017.
 */
class NasaTVApi {

    companion object {
        val NASA_TV_ID: String = "10414700"
    }

    private val channelService: ChannelService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ustream.tv/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        channelService = retrofit.create(ChannelService::class.java)
    }

    fun getChannel() : Call<ChannelResponse> {
        return channelService.getChannels(NasaTVApi.NASA_TV_ID)
    }
}