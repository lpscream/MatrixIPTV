package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.data.channels.cache.ChannelsCacheDataSource
import net.matrixhome.matrixiptv.data.channels.cache.ChannelsCacheMapper
import net.matrixhome.matrixiptv.data.Settings
import java.util.*


interface ChannelsRepository {
    suspend fun fetchChannels(): ChannelsData
    suspend fun filterOutChannels(category: String): ChannelsData
    class Base(
        private val cloudDataSource: ChannelsCloudDataSource,
        private val cacheDataSource: ChannelsCacheDataSource,
        private val channelsCloudMapper: ChannelsCloudMapper,
        private val channelsCacheMapper: ChannelsCacheMapper,
        private val settings: Settings
    ): ChannelsRepository {
        override suspend fun fetchChannels() = try {
            val channelsCacheList = cacheDataSource.fetchChannels()
            val channelCloudList = cloudDataSource.fetchChannels() //works
            val channels = channelsCloudMapper.map(channelCloudList) //works
            if (channelsCacheList.isEmpty()){
                cacheDataSource.saveChannels(channels)
                if (channelCloudList.isEmpty()){
                    ChannelsData.Fail(throw EmptyStackException())
                }else{
                    filterOutChannels(settings.fetchCategoryName())
                    //ChannelsData.Success(channelsCacheMapper.map(channelCloudList))
                }
            }else{
                if (channelCloudList.isEmpty()){
                    ChannelsData.Fail(throw EmptyStackException())
                }else{
                    //cacheDataSource.saveChannels(channels)
                    filterOutChannels(settings.fetchCategoryName())
                    //ChannelsData.Success(channelsCacheMapper.map(channelsCacheList))
                }
            }
        }catch (e: Exception){
            ChannelsData.Fail(e)
        }


        override suspend fun filterOutChannels(category: String) = try {
            val channelsCacheList = cacheDataSource.fetchSortedChannels(category)
            if (channelsCacheList.isEmpty()){
                //fixme needs database error
                ChannelsData.Fail(throw EmptyStackException())
                //fetchChannels()
            }else{
                ChannelsData.Success(channelsCacheMapper.map(channelsCacheList))
            }
        }catch (e: Exception){
            ChannelsData.Fail(e)
        }
    }
}