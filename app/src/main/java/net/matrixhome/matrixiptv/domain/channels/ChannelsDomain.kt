package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ChannelDataToDomainMapper
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.channels.ChannelsUi
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.*

sealed class ChannelsDomain : Abstract.Object<ChannelsUi, ChannelsDomainToUiMapper> {
    class Success(
        private val channels: List<ChannelData>,
        private val channelMapper: ChannelDataToDomainMapper
    ) : ChannelsDomain() {
        override fun map(mapper: ChannelsDomainToUiMapper) = mapper.map(channels.map {
            it.map(channelMapper)
        })
    }

    class Fail(private val e: Exception) : ChannelsDomain() {
        override fun map(mapper: ChannelsDomainToUiMapper) = mapper.map(
            when (e) {
                is UnknownHostException -> ErrorType.NO_CONNECTION
                is HttpException -> ErrorType.SERVICE_UNAVAILABLE
                is EmptyStackException -> ErrorType.EMPTY_LIST
                else -> ErrorType.GENERIC_ERROR
            }
        )
    }
}



