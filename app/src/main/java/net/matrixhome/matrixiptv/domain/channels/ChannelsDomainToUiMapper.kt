package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.channels.ChannelsUi

interface ChannelsDomainToUiMapper: Abstract.Mapper {
    fun map(channels: List<ChannelDomain>): ChannelsUi
    fun map(errorType: ErrorType): ChannelsUi
    //fun map(id: Int, number: Int,url: String, title: String, genre: String): ChannelUI
}