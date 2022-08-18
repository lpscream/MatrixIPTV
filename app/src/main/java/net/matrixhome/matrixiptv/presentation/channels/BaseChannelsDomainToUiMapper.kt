package net.matrixhome.matrixiptv.presentation.channels

import net.matrixhome.matrixiptv.domain.channels.ChannelDomain
import net.matrixhome.matrixiptv.domain.channels.ChannelDomainToUiMapper
import net.matrixhome.matrixiptv.domain.channels.ChannelsDomainToUiMapper
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.ResourceProvider

class BaseChannelsDomainToUiMapper(
    private val resourceProvider: ResourceProvider,
    private val channelMapper: ChannelDomainToUiMapper
): ChannelsDomainToUiMapper {
    override fun map(channels: List<ChannelDomain>) = ChannelsUi.Success(channels, channelMapper)

    override fun map(errorType: ErrorType) = ChannelsUi.Fail(errorType, resourceProvider)
}