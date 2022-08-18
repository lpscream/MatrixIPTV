package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammDb
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammDbWrapper
import net.matrixhome.matrixiptv.domain.programms.ProgrammDomain

data class ProgrammData(
    val id: String,
    private val title: String,
    private val start: Long,
    private val stop: Long
): ToProgrammDb<ProgrammDb, ProgrammDataToDbMapper>,
        Abstract.Object<ProgrammDomain, ProgrammDataToDomainMapper>{

    override fun map(mapper: ProgrammDataToDomainMapper)
    = mapper.map(id, title, start, stop)

    override fun mapTo(mapper: ProgrammDataToDbMapper, db: ProgrammDbWrapper)
    = mapper.mapToDb(id, title, start, stop, db)
}


interface ToProgrammDb<T, M : Abstract.Mapper>{
    fun mapTo(mapper: M, db: ProgrammDbWrapper): T
}
