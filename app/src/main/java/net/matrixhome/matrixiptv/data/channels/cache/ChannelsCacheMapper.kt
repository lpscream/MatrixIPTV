package net.matrixhome.matrixiptv.data.channels.cache

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ToChannelMapper

interface ChannelsCacheMapper: Abstract.Mapper {

    fun map(channels: List<Abstract.Object<ChannelData, ToChannelMapper>>): List<ChannelData>

    class Base(private val mapper: ToChannelMapper): ChannelsCacheMapper {
        override fun map(channels: List<Abstract.Object<ChannelData, ToChannelMapper>>) =
            channels.map { channelsDb ->
                channelsDb.map(mapper)
            }
    }
}