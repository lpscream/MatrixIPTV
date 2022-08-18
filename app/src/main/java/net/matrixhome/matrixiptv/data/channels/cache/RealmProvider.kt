package net.matrixhome.matrixiptv.data.channels.cache

import io.realm.Realm
import io.realm.RealmConfiguration

interface RealmProvider {

    fun provide(): Realm

    class Base: RealmProvider {
        override fun provide(): Realm = Realm.getInstance(realmConfig)
    }


    companion object {
        val realmConfig = RealmConfiguration.Builder()
            .schemaVersion(1)
            .build()
    }
}