package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.channels.ChannelsDomain
import java.lang.Exception

sealed class ChannelsData: Abstract.Object<
        ChannelsDomain,
        ChannelsDataToDomainMapper> {
     data class Success(private val channels: List<ChannelData>): ChannelsData(){
         override fun map(mapper: ChannelsDataToDomainMapper) =
             mapper.map(channels)
     }

    data class Fail(private val e: Exception): ChannelsData(){
        override fun map(mapper: ChannelsDataToDomainMapper) =
            mapper.map(e)
    }
}