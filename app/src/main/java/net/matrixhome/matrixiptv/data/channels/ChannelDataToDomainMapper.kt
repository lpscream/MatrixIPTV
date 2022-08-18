package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.channels.ChannelDomain

interface ChannelDataToDomainMapper: Abstract.Mapper {
//    fun map(channels: List<Channel>): ChannelDomain
    fun map (id: Int,
             number: Int,
             url: String,
             title: String,
             genre: String): ChannelDomain
}