package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract

interface ToChannelMapper: Abstract.Mapper {

    fun map(id: Int,
            number: Int,
            url: String,
            title: String,
            genre: String): ChannelData

    class Base: ToChannelMapper {
        override fun map(
            id: Int,
            number: Int,
            url: String,
            title: String,
            genre: String
        ) = ChannelData(id, number, url, title, genre)
    }
}