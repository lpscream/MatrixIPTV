package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.domain.programms.ProgrammsDomain
import java.lang.Exception

sealed class ProgrammsData: Abstract.Object<ProgrammsDomain, ProgrammsDataToDomainMapper> {

    data class Success(private val programms: List<ProgrammData>): ProgrammsData(){
        override fun map(mapper: ProgrammsDataToDomainMapper) =
            mapper.map(programms)
    }


    data class Fail(private val e: Exception): ProgrammsData(){
        override fun map(mapper: ProgrammsDataToDomainMapper) =
            mapper.map(e)

    }

}