package net.matrixhome.matrixiptv.presentation.programms

import net.matrixhome.matrixiptv.core.Abstract

sealed class ProgrammUI : Abstract.Object<Unit, ProgrammUI.StringMapper> {
    override fun map(mapper: StringMapper) = Unit

    object Progress : ProgrammUI()

    class Base(
        private val id: String,
        private val title: String,
        private val start: Long,
        private val stop: Long
    ) : ProgrammUI() {
        override fun map(mapper: StringMapper) = mapper.map(id, title, start, stop)
    }

    class Fail(private val message: String) : ProgrammUI() {
        override fun map(mapper: StringMapper) = mapper.map("", message, -1, -1)
    }

    interface StringMapper : Abstract.Mapper {
        fun map(id: String, title: String, start: Long, stop: Long)
    }
}