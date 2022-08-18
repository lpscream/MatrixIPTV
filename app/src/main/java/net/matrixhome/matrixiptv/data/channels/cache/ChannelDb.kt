package net.matrixhome.matrixiptv.data.channels.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ToChannelMapper


//Channel(id=, number=, url=, title=, genre=)
open class ChannelDb: RealmObject(), Abstract.Object<ChannelData, ToChannelMapper> {

    @PrimaryKey
    var id: Int = -1
    var number: Int = -1
    var url: String = ""
    var title: String = ""
    var genre: String = ""


    override fun map(mapper: ToChannelMapper) = ChannelData(
        id,
        number,
        url,
        title,
        genre
    )
}