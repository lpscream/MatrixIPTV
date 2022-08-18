package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomain

interface ProgrammDataToDomainMapper: Abstract.Mapper {

    fun map(
        id: String,
        title: String,
        start: Long,
        stop: Long
    ): ProgrammDomain
}