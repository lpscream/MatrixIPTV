package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.channels.ChannelsDomain
import java.lang.Exception

interface ChannelsDataToDomainMapper: Abstract.Mapper {

    fun map(channels: List<ChannelData>): ChannelsDomain
    fun map(e: Exception): ChannelsDomain
}