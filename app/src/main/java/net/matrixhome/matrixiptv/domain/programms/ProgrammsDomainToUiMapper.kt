package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.ErrorType
import net.matrixhome.matrixiptv.presentation.programms.ProgrammsUi

interface ProgrammsDomainToUiMapper : Abstract.Mapper {
    fun map (programms: List<ProgrammDomain>): ProgrammsUi
    fun map (errorType: ErrorType): ProgrammsUi
}