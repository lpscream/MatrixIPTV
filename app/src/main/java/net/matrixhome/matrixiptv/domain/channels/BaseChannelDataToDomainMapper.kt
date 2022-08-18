package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.data.channels.ChannelDataToDomainMapper

class BaseChannelDataToDomainMapper: ChannelDataToDomainMapper {
    override fun map(
        id: Int,
        number: Int,
        url: String,
        title: String,
        genre: String
    ) = ChannelDomain(id,number, url, title,genre)
}