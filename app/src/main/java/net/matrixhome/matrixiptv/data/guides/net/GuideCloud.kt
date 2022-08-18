package net.matrixhome.matrixiptv.data.guides.net

import com.google.gson.annotations.SerializedName
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.guides.ProgrammData
import net.matrixhome.matrixiptv.data.guides.ToProgrammMapper


/*
{
    "channels": [
        {
            "id": id канала,
            "name": наименование канала,
            "icon": ссылка на иконку канала
        }, ...
    ],
    "programms": [
        {
            "id": id программы,
            "title": название программы,
            "start": начало (timestamp),
            "stop": окончание (taimstamp),
            "channel": id канала
        }, ...
    ]
}
 */

data class GuideCloud(
    @SerializedName("channels")
    val channel: Channel,
    @SerializedName("programms")
    val programms: List<ProgrammCloud> = emptyList()
)


data class Channel(
    //todo maybe here we got some mistakes)))
    //fuck it out
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String
)



data class ProgrammCloud(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("start")
    val start: Long,
    @SerializedName("stop")
    val stop: Long,
    @SerializedName("channel")
    val channel: String
): Abstract.Object<ProgrammData, ToProgrammMapper>{
    override fun map(mapper: ToProgrammMapper) =
        mapper.map(id, title, start, stop)
}


