package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.cache.ChannelDb
import net.matrixhome.matrixiptv.data.channels.cache.DbWrapper

interface ChannelDataToDbMapper : Abstract.Mapper {
    fun mapToDb(
        id: Int,
        number: Int,
        url: String,
        title: String,
        genre: String,
        db: DbWrapper
    ): ChannelDb

    class Base : ChannelDataToDbMapper {
        override fun mapToDb(
            id: Int, number: Int, url: String, title: String, genre: String, db: DbWrapper
        ): ChannelDb {
            val channelDb = db.createObject(id)
            channelDb.number = number
            channelDb.url = url
            channelDb.title = title
            channelDb.genre = genre
            return channelDb
        }
    }
}