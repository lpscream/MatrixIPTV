package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI


//Channel(id=, number=, url=, title=, genre=)
class ChannelDomain(private val id: Int, private val number: Int, private val url: String, private val title: String, private val genre: String):
        Abstract.Object<ChannelUI, ChannelDomainToUiMapper>{
    override fun map(mapper: ChannelDomainToUiMapper) = mapper.map(id, number,url, title, genre)

}