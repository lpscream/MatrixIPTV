package net.matrixhome.matrixiptv.data.guides.cache

import net.matrixhome.matrixiptv.data.channels.cache.RealmProvider
import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ProgrammDataToDbMapper

interface ProgrammsCacheDataSource {

    fun fetchProgramms(): List<ProgrammDb>

    fun saveProgramms(programms: List<ProgrammData>)


    //fixme is it needed new realm provider or not??
    class Base(private val realmProvider: RealmProvider,
    private val mapper: ProgrammDataToDbMapper): ProgrammsCacheDataSource {
        override fun fetchProgramms(): List<ProgrammDb> {
            var obj: List<ProgrammDb> = emptyList()
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    val programmsDb = it.where(ProgrammDb::class.java).findAll()
                    obj = it.copyFromRealm(programmsDb)
                }
                return obj
            }
        }

        override fun saveProgramms(programms: List<ProgrammData>) {
            realmProvider.provide().use { realm ->
                realm.executeTransaction {
                    programms.forEach { programm ->
                        programm.mapTo(mapper, ProgrammDbWrapper.Base(it))
                    }
                }
            }
        }
    }
}