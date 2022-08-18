package net.matrixhome.matrixiptv.presentation.channels

import net.matrixhome.matrixiptv.core.Abstract

sealed class ChannelUI : Abstract.Object<Unit, ChannelUI.StringMapper> {
    override fun map(mapper: StringMapper) = Unit

    object Progress : ChannelUI()

    class Base(
        private val id: Int,
        private val number: Int,
        private val url: String,
        private val title: String,
        private val genre: String
    ) : ChannelUI() {
        override fun map(mapper: StringMapper) = mapper.map(id, title, url, number)
    }

    class Fail(private val message: String) : ChannelUI() {
        override fun map(mapper: StringMapper) = mapper.map(-1, message, "", -1)
    }

    interface StringMapper : Abstract.Mapper {
//        fun map(text: String)
//        fun map(text: String, url: String)
        fun map(id: Int, name: String, url: String, number: Int)
    }


}