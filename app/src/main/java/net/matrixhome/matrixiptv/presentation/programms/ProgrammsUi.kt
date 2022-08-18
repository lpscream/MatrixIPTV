package net.matrixhome.matrixiptv.presentation.programms

import net.matrixhome.matrixiptv.R
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomain
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomainToUIMapper
import net.matrixhome.matrixiptv.presentation.ResourceProvider

sealed class ProgrammsUi : Abstract.Object<Unit, ProgrammsCommunication> {


    class Success(
        private val programms: List<ProgrammDomain>,
        private val programmMapper: ProgrammDomainToUIMapper
    ): ProgrammsUi() {
        override fun map(mapper: ProgrammsCommunication) {
            val programmsUi = programms.map {
                it.map(programmMapper)
            }
            mapper.map(programmsUi)
        }
    }



    class Fail(
        private val errorType: ErrorType,
        private val resourseProvider: ResourceProvider
    ) : ProgrammsUi() {
        override fun map(mapper: ProgrammsCommunication) {
            val messageId = when (errorType) {
                ErrorType.NO_CONNECTION -> R.string.no_connection_message
                ErrorType.SERVICE_UNAVAILABLE -> R.string.service_unavailable_message
                ErrorType.EMPTY_LIST -> R.string.empty_list
                else -> R.string.other_trouble_message
            }
            val message = resourseProvider.getString(messageId)
            mapper.map(listOf(ProgrammUI.Fail(message)))
        }
    }
}