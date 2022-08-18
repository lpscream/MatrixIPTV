package net.matrixhome.matrixiptv.domain.channels

import net.matrixhome.matrixiptv.data.channels.ChannelsDataToDomainMapper
import net.matrixhome.matrixiptv.data.channels.ChannelsRepository
import net.matrixhome.matrixiptv.data.guides.ProgrammsDataToDomainMapper
import net.matrixhome.matrixiptv.data.guides.ProgrammsRepository
import net.matrixhome.matrixiptv.domain.programms.ProgrammsDomain

interface ChannelsInteractor {

    suspend fun fetchChannels(): ChannelsDomain

    suspend fun fetchSortedChannels(category: String): ChannelsDomain

    suspend fun fetchProgramms(id: String): ProgrammsDomain


    class Base(
        private val channelsRepository: ChannelsRepository,
        private val mapper: ChannelsDataToDomainMapper,
        private val programmsRepository: ProgrammsRepository,
        private val mapperGuide: ProgrammsDataToDomainMapper
    ) : ChannelsInteractor {
        override suspend fun fetchChannels() = channelsRepository.fetchChannels().map(mapper)

        override suspend fun fetchSortedChannels(category: String) =
            channelsRepository.filterOutChannels(category).map(mapper)

        override suspend fun fetchProgramms(id: String) =
            programmsRepository.fetchProgramms(id).map(mapperGuide)
    }
}