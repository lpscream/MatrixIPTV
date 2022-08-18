package net.matrixhome.matrixiptv.presentation.channels

import net.matrixhome.matrixiptv.R
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.channels.ChannelDomain
import net.matrixhome.matrixiptv.domain.channels.ChannelDomainToUiMapper
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.ResourceProvider


sealed class ChannelsUi : Abstract.Object<Unit, ChannelsCommunication> {


    class Success(
        private val channels: List<ChannelDomain>,
        private val channelMapper: ChannelDomainToUiMapper
    ) : ChannelsUi() {
        override fun map(mapper: ChannelsCommunication) {
            val channelsUi = channels.map {
                it.map(channelMapper)
            }
            mapper.map(channelsUi)
        }
    }

    class Fail(
        private val errorType: ErrorType,
        private val resourseProvider: ResourceProvider
    ) : ChannelsUi() {
        override fun map(mapper: ChannelsCommunication) {
            val messageId = when (errorType) {
                ErrorType.NO_CONNECTION -> R.string.no_connection_message
                ErrorType.SERVICE_UNAVAILABLE -> R.string.service_unavailable_message
                ErrorType.EMPTY_LIST -> R.string.empty_list
                else -> R.string.other_trouble_message
            }
            val message = resourseProvider.getString(messageId)
            mapper.map(listOf(ChannelUI.Fail(message)))
        }
    }
}