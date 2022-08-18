package net.matrixhome.matrixiptv.presentation.channels

import net.matrixhome.matrixiptv.domain.channels.ChannelDomainToUiMapper

class BaseChannelDomainToUiMapper: ChannelDomainToUiMapper {
    override fun map(id: Int, number: Int, url: String, title: String, genre: String) =
        ChannelUI.Base(id, number, url, title, genre)
}