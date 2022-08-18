package net.matrixhome.matrixiptv.data.channels.cache

import io.realm.Realm

interface DbWrapper {

    fun createObject(id: Int): ChannelDb


    class Base(private val realm: Realm): DbWrapper {
        override fun createObject(id: Int): ChannelDb {
            //todo needs id to create object
            //return realm.copyToRealmOrUpdate(ChannelDb::class.java)
            return realm.createObject(ChannelDb::class.java, id)
        }
    }
}