package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI

interface ChannelDomainToUiMapper: Abstract.Mapper {

    fun map (id: Int, number: Int,url: String, title: String, genre: String): ChannelUI
}