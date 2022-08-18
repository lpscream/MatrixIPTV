package net.matrixhome.matrixiptv.data.guides.cache

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ToProgrammMapper

interface ProgrammsCacheMapper : Abstract.Mapper {

    fun map(programms: List<Abstract.Object<ProgrammData, ToProgrammMapper>>): List<ProgrammData>

    class Base(private val mapper: ToProgrammMapper) : ProgrammsCacheMapper {
        override fun map(programms: List<Abstract.Object<ProgrammData, ToProgrammMapper>>) =
            programms.map { programmsDb ->
                programmsDb.map(mapper)
            }
    }
}