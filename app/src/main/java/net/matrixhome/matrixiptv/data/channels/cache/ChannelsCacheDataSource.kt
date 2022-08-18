package net.matrixhome.matrixiptv.data.channels.cache

import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ChannelDataToDbMapper

interface ChannelsCacheDataSource {

    fun fetchChannels(): List<ChannelDb>

    fun saveChannels(channels: List<ChannelData>)

    fun fetchSortedChannels(category: String): List<ChannelDb>

    fun fetchPlaylist(): List<String>


    class Base(
        private val realmProvider: RealmProvider,
        private val mapper: ChannelDataToDbMapper
    ) : ChannelsCacheDataSource {
        private val TAG = "ChannelsCacheDataSource"
        override fun fetchChannels(): List<ChannelDb> {
            var obj: List<ChannelDb> = emptyList()
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    val channelsDb = it.where(ChannelDb::class.java).findAll()
                    obj = it.copyFromRealm(channelsDb)
                }
                return obj
            }
        }

        override fun saveChannels(channels: List<ChannelData>) =
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    channels.forEach { channel ->
                        //it.where(ChannelDb::class.java).equalTo("id", channel.id).findFirst()?.deleteFromRealm()
                        channel.mapTo(mapper, DbWrapper.Base(it))
                    }
                }
            }

        override fun fetchSortedChannels(category: String): List<ChannelDb> {
            var obj: List<ChannelDb> = emptyList()
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    if (category != "Все"){
                        val channelsDb = it.where(ChannelDb::class.java).equalTo("genre", category).findAll()
                        obj = it.copyFromRealm(channelsDb)
                    }else{
                        val channelsDb = it.where(ChannelDb::class.java).findAll()
                        obj = it.copyFromRealm(channelsDb)
                    }
                }
                return obj
            }
        }

        override fun fetchPlaylist(): List<String> {
            var obj: List<String> = emptyList()
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    val channelsDb = it.where(ChannelDb::class.java).findAll()
                    //obj = it.copyFromRealm(channelsDb)
                }
                return obj
            }
        }


    }
}


