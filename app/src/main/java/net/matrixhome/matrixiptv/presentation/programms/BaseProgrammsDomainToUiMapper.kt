package net.matrixhome.matrixiptv.presentation.programms

import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomain
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomainToUIMapper
import net.matrixhome.matrixiptv.domain.programms.ProgrammsDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.ResourceProvider

class BaseProgrammsDomainToUiMapper(
    private val resourceProvider: ResourceProvider,
    private val programmMapper: ProgrammDomainToUIMapper): ProgrammsDomainToUiMapper {
    override fun map(programms: List<ProgrammDomain>) = ProgrammsUi.Success(programms, programmMapper)

    override fun map(errorType: ErrorType) = ProgrammsUi.Fail(errorType, resourceProvider)
}