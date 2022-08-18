package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.data.guides.ProgrammDataToDomainMapper

class BaseProgrammDataToDomainMapper : ProgrammDataToDomainMapper {
    override fun map(id: String, title: String, start: Long, stop: Long) =

        ProgrammDomain(id, title, start, stop)
}