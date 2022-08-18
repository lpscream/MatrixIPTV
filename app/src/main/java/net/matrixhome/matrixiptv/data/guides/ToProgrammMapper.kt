package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract

interface ToProgrammMapper: Abstract.Mapper {

    fun map(id: String,
    title: String,
    start: Long,
    stop: Long): ProgrammData


    class Base: ToProgrammMapper{
        override fun map(id: String, title: String, start: Long, stop: Long) =
            ProgrammData(id, title, start, stop)
    }
}