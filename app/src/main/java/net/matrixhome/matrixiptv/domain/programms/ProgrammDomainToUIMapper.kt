package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.presentation.programms.ProgrammUI

interface ProgrammDomainToUIMapper: Abstract.Mapper {

    fun map (id: String, title: String, start: Long, stop: Long): ProgrammUI
}