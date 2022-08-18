package net.matrixhome.matrixiptv.data.guides

import net.matrixhome.matrixiptv.data.guides.cache.ProgrammsCacheDataSource
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammsCacheMapper
import java.util.*

interface ProgrammsRepository {
    suspend fun fetchProgramms(id: String): ProgrammsData



    class Base(
        private val cacheDataSource: ProgrammsCacheDataSource,
        private val cloudDataSource: ProgrammsCloudDataSource,
        private val programmsCloudMapper: ProgrammsCloudMapper,
        private val programmsCacheMapper: ProgrammsCacheMapper
    ):ProgrammsRepository {



        override suspend fun fetchProgramms(id: String) = try {
            //fixme if needed
            //val programmsCacheList = cacheDataSource.fetchProgramms()
            val programmsCloudList = cloudDataSource.fetchProgramms(id)
            val programms = programmsCloudMapper.map(programmsCloudList)
            if (programmsCloudList.isEmpty()){
                ProgrammsData.Fail(throw EmptyStackException())
            }else{
                ProgrammsData.Success(programmsCloudMapper.map(programmsCloudList))
            }
        }catch (e: Exception){
            ProgrammsData.Fail(e)
        }
    }
}