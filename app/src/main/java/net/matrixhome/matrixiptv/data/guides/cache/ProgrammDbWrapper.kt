package net.matrixhome.matrixiptv.data.guides.cache

import io.realm.Realm

interface ProgrammDbWrapper {

    fun createObject(id: String): ProgrammDb


    class Base(private val realm: Realm): ProgrammDbWrapper {
        override fun createObject(id: String): ProgrammDb {
            return  realm.createObject(ProgrammDb::class.java, id)
        }
    }
}