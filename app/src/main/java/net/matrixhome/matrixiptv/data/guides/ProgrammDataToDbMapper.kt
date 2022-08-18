package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammDb
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammDbWrapper

interface ProgrammDataToDbMapper : Abstract.Mapper {
    fun mapToDb(
        id: String,
        title: String,
        start: Long,
        stop: Long,
        db: ProgrammDbWrapper
    ): ProgrammDb

    class Base() : ProgrammDataToDbMapper {
        override fun mapToDb(
            id: String, title: String, start: Long, stop: Long, db: ProgrammDbWrapper
        ): ProgrammDb {
            val programmDb = db.createObject(id)
            programmDb.title = title
            programmDb.start = start
            programmDb.stop = stop
            return programmDb
        }
    }
}