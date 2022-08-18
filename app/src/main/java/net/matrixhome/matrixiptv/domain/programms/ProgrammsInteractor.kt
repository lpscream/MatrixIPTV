package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.data.guides.ProgrammsDataToDomainMapper
import net.matrixhome.matrixiptv.data.guides.ProgrammsRepository

interface ProgrammsInteractor {
    suspend fun fetchProgramms(id: String): ProgrammsDomain


    class Base(
        private val programmsRepository: ProgrammsRepository,
        private val mapper: ProgrammsDataToDomainMapper
    ): ProgrammsInteractor{
        override suspend fun fetchProgramms(id: String) =
            programmsRepository.fetchProgramms(id).map(mapper)
    }
}