package net.matrixhome.matrixiptv.data.channels.net

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers

interface ChannelsService {

    @Headers("Accept: application/json")
    @GET("/playlist/?media=mag250&link_format=hls")
    suspend fun fetchChannels(): ResponseBody
}