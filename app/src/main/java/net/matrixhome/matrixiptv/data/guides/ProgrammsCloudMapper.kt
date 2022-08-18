package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.core.Abstract

interface ProgrammsCloudMapper : Abstract.Mapper {
    fun map(cloudList: List<Abstract.Object<ProgrammData, ToProgrammMapper>>): List<ProgrammData>


    class Base(
        private val programmMapper: ToProgrammMapper
    ) : ProgrammsCloudMapper {
        override fun map(cloudList: List<Abstract.Object<ProgrammData, ToProgrammMapper>>) =
            cloudList.map { programmCloud ->
                programmCloud.map(programmMapper)
            }
    }
}