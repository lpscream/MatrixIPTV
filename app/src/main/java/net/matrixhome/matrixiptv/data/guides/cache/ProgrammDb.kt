package net.matrixhome.matrixiptv.data.guides.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ToProgrammMapper

open class ProgrammDb: RealmObject(), Abstract.Object<ProgrammData, ToProgrammMapper> {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var start: Long = -1
    var stop: Long = -1


    override fun map(mapper: ToProgrammMapper)
    = ProgrammData(id, title, start, stop)
}