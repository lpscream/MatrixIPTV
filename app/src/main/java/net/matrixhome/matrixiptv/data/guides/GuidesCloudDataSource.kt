package net.matrixhome.matrixiptv.data.guides

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.matrixhome.matrixiptv.data.guides.net.GuideCloud
import net.matrixhome.matrixiptv.data.guides.net.GuideService
import net.matrixhome.matrixiptv.data.guides.net.ProgrammCloud

interface GuidesCloudDataSource {


    suspend fun fetchGuides():List<ProgrammCloud>


    class Base(
        private val service: GuideService,
        private val gson: Gson
    ):GuidesCloudDataSource {
        private val typeToken = object : TypeToken<GuideCloud>(){}.type
        override suspend fun fetchGuides(): List<ProgrammCloud> {
            val obj: GuideCloud = gson.fromJson(service.fetchGuides().string(), typeToken)
            return obj.programms
        }
    }
}