package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract

interface ChannelsCloudMapper : Abstract.Mapper {

    fun map(cloudList: List<Abstract.Object<ChannelData, ToChannelMapper>>): List<ChannelData>

    class Base(private val channelMapper: ToChannelMapper) : ChannelsCloudMapper {
        override fun map(cloudList: List<Abstract.Object<ChannelData, ToChannelMapper>>) =
            cloudList.map { channelCloud ->
                channelCloud.map(channelMapper)
            }
    }
}