package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.presentation.programms.ProgrammUI

class ProgrammDomain(
    private val id: String,
    private val title: String,
    private val start: Long,
    private val stop: Long
    ): Abstract.Object<ProgrammUI, ProgrammDomainToUIMapper> {

    override fun map(mapper: ProgrammDomainToUIMapper) = mapper.map(id, title, start, stop)
}