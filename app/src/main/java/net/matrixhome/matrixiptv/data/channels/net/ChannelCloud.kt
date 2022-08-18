package net.matrixhome.matrixiptv.data.channels.net

import com.google.gson.annotations.SerializedName
import net.matrixhome.matrixiptv.core.Abstract
import net.matrixhome.matrixiptv.data.channels.ChannelData
import net.matrixhome.matrixiptv.data.channels.ToChannelMapper


//Channel(id=, number=, url=, title=, genre=)



data class ChannelsList(
    @SerializedName("channels")
    val channels: List<ChannelCloud>
)

data class ChannelCloud(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("number")
    private val number: Int,
    @SerializedName("url")
    private val url: String,
    @SerializedName("title")
    private val title: String,
    @SerializedName("genre")
    private val genre: String
): Abstract.Object<ChannelData, ToChannelMapper>{
    override fun map(mapper: ToChannelMapper) =
        mapper.map(id, number, url, title, genre)
}


