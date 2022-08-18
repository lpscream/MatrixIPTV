package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ProgrammDataToDomainMapper
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.programms.ProgrammsUi
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException
import java.util.*

sealed class ProgrammsDomain : Abstract.Object<ProgrammsUi, ProgrammsDomainToUiMapper> {

    data class Success(
        private val programms: List<ProgrammData>,
        private val programmMapper: ProgrammDataToDomainMapper
    ) : ProgrammsDomain() {
        override fun map(mapper: ProgrammsDomainToUiMapper) =
            mapper.map(
                programms.map {
                    it.map(programmMapper)
                }
            )
    }

    class Fail(private val e: Exception) : ProgrammsDomain(){
        override fun map(mapper: ProgrammsDomainToUiMapper) = mapper.map(
            when (e) {
                is UnknownHostException -> ErrorType.NO_CONNECTION
                is HttpException -> ErrorType.SERVICE_UNAVAILABLE
                is EmptyStackException -> ErrorType.EMPTY_LIST
                else -> ErrorType.GENERIC_ERROR
            }
        )
    }
}