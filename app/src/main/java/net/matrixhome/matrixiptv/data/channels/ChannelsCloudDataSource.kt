package net.matrixhome.matrixiptv.data.channels

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.matrixhome.matrixiptv.data.channels.net.ChannelCloud
import net.matrixhome.matrixiptv.data.channels.net.ChannelsList
import net.matrixhome.matrixiptv.data.channels.net.ChannelsService

interface ChannelsCloudDataSource {

    suspend fun fetchChannels(): List<ChannelCloud>

    class Base(private val service: ChannelsService,
               private val gson: Gson): ChannelsCloudDataSource {
        private val type = object : TypeToken<ChannelsList>() {}.type

        override suspend fun fetchChannels(): List<ChannelCloud>{
            val obj: ChannelsList = gson.fromJson(service.fetchChannels().string(), type)
            return obj.channels
        }
    }
}