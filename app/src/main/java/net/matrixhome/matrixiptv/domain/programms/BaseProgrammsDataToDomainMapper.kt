package net.matrixhome.matrixiptv.domain.programms

import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ProgrammDataToDomainMapper
import net.matrixhome.matrixiptv.data.guides.ProgrammsDataToDomainMapper

class BaseProgrammsDataToDomainMapper(
    private val programmMapper: ProgrammDataToDomainMapper):
            ProgrammsDataToDomainMapper{
    override fun map(programms: List<ProgrammData>) = ProgrammsDomain.Success(programms, programmMapper)

    override fun map(e: Exception) = ProgrammsDomain.Fail(e)
}