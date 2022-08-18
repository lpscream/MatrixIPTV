package net.matrixhome.matrixiptv.data.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.cache.ChannelDb
import net.matrixhome.matrixiptv.data.channels.cache.DbWrapper
import net.matrixhome.matrixiptv.domain.channels.ChannelDomain

data class ChannelData(
    val id: Int,
    private val number: Int,
    private val url: String,
    private val title: String,
    private val genre: String
) : ToChannelDb<ChannelDb, ChannelDataToDbMapper>,
    Abstract.Object<ChannelDomain, ChannelDataToDomainMapper> {

    override fun map(mapper: ChannelDataToDomainMapper) =
        mapper.map(id, number, url, title, genre)

    override fun mapTo(mapper: ChannelDataToDbMapper, db: DbWrapper) =
        mapper.mapToDb(id, number, url, title, genre, db)
}

interface ToChannelDb<T, M : Abstract.Mapper> {
    fun mapTo(mapper: M, db: DbWrapper): T
}