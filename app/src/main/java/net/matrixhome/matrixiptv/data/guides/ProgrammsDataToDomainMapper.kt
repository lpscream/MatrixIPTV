package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.programms.ProgrammsDomain

interface ProgrammsDataToDomainMapper: Abstract.Mapper {

    fun map(programms: List<ProgrammData>): ProgrammsDomain
    fun map(e: Exception): ProgrammsDomain
}