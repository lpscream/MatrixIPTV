package net.matrixhome.matrixiptv.data.guides

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.matrixhome.matrixiptv.data.guides.net.GuideCloud
import net.matrixhome.matrixiptv.data.guides.net.GuideService
import net.matrixhome.matrixiptv.data.guides.net.ProgrammCloud
import java.text.SimpleDateFormat
import java.util.*

interface ProgrammsCloudDataSource {

    suspend fun fetchProgramms(id: String): List<ProgrammCloud>

    class Base(
        private val service: GuideService,
        private val gson: Gson
    ): ProgrammsCloudDataSource{
        private val TAG = "ProgrammsCldDataSrc_log"
        private val type = object : TypeToken<GuideCloud>() {}.type
        override suspend fun fetchProgramms(id: String): List<ProgrammCloud> {
            val obj: GuideCloud = gson.fromJson(service.fetchGuides(channel_id = id,
                date_from = toDate(System.currentTimeMillis()),
                date_to = toDate((System.currentTimeMillis()) + 604800000)).string(), type)
            //fixme add date list separator
            val toSend = mutableListOf<ProgrammCloud>()
            var toCompare = "1980-01-01"
            obj.programms.forEach {
                if (toCompare.equals(toDate(it.start * 1000))){
                    toSend.add(it)
                }else{
                    toCompare = toDate(it.start * 1000)
                    toSend.add(ProgrammCloud("date", toDate(it.start * 1000),-1,-1,""))
                    toSend.add(it)
                }
            }
            return toSend
        }

        private fun toDate(date: Long): String{
            val toSend = SimpleDateFormat("yyyy-MM-dd")
                .format(Date((date)))
            return toSend
        }
            
    }
}