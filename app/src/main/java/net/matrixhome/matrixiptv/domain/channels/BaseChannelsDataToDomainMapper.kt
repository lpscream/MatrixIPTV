package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ChannelDataToDomainMapper
import net.matrixhome.matrixiptv.data.channels.ChannelsDataToDomainMapper
import java.lang.Exception

class BaseChannelsDataToDomainMapper(private val channelMapper: ChannelDataToDomainMapper) :
    ChannelsDataToDomainMapper {
    override fun map(channels: List<ChannelData>) = ChannelsDomain.Success(channels, channelMapper)

    override fun map(e: Exception) = ChannelsDomain.Fail(e)
}