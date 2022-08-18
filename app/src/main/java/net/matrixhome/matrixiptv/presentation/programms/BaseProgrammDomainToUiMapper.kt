package net.matrixhome.matrixiptv.presentation.programms

import net.matrixhome.matrixiptv.domain.programms.ProgrammDomainToUIMapper

class BaseProgrammDomainToUiMapper: ProgrammDomainToUIMapper {
    override fun map(id: String, title: String, start: Long, stop: Long) =
        ProgrammUI.Base(id, title, start, stop)
}