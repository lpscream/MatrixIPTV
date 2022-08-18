package net.matrixhome.matrixiptv.data.guides.net

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideService {

//    @Headers("Accept: application/json")
//    @GET("/api/epg/?date=2022-05-28&channel_id=107&format=json")
//    suspend fun fetchGuides(): ResponseBody

    @Headers("Accept: application/json")
    @GET("/api/epg/")
    suspend fun fetchGuides(
        @Query("date_from") date_from: String = "2022-05-29",
        @Query("date_to") date_to: String = "2022-06-06",
        @Query("channel_id") channel_id: String = "",
        @Query("format") format: String = "json"
    ): ResponseBody
}