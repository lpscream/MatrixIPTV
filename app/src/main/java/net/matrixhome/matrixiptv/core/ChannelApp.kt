package net.matrixhome.matrixiptv.core

import android.app.Application
import com.google.gson.Gson
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import io.realm.Realm
import net.matrixhome.matrixiptv.data.channels.cache.ChannelsCacheDataSource
import net.matrixhome.matrixiptv.data.channels.cache.ChannelsCacheMapper
import net.matrixhome.matrixiptv.data.channels.cache.RealmProvider
import net.matrixhome.matrixiptv.data.channels.*
import net.matrixhome.matrixiptv.data.channels.net.ChannelsService
import net.matrixhome.matrixiptv.data.guides.*
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammsCacheDataSource
import net.matrixhome.matrixiptv.data.guides.cache.ProgrammsCacheMapper
import net.matrixhome.matrixiptv.data.guides.net.GuideService
import net.matrixhome.matrixiptv.domain.channels.BaseChannelDataToDomainMapper
import net.matrixhome.matrixiptv.domain.channels.BaseChannelsDataToDomainMapper
import net.matrixhome.matrixiptv.domain.channels.ChannelsInteractor
import net.matrixhome.matrixiptv.domain.programms.BaseProgrammDataToDomainMapper
import net.matrixhome.matrixiptv.domain.programms.BaseProgrammsDataToDomainMapper
import net.matrixhome.matrixiptv.presentation.*
import net.matrixhome.matrixiptv.presentation.channels.BaseChannelDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.channels.BaseChannelsDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.channels.ChannelsCommunication
import net.matrixhome.matrixiptv.presentation.programms.BaseProgrammDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.programms.BaseProgrammsDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.programms.ProgrammsCommunication
import net.matrixhome.matrixiptv.data.Settings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class ChannelApp: Application() {



    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val builder : Picasso.Builder = Picasso.Builder(this)
        builder .downloader(OkHttp3Downloader(this, Int.MAX_VALUE.toLong()))
        val build: Picasso = builder.build()
        Picasso.setSingletonInstance(build)

        val settings = Settings(applicationContext)

        Realm.init(this)
        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .build()
        val service = retrofit.create(ChannelsService::class.java)
        // FIXME: 19.05.2022 should be only one retrofit service
        val guidesService = retrofit.create(GuideService::class.java)

        val gson = Gson()


        val cloudDataSource = ChannelsCloudDataSource.Base(gson = gson, service = service)
        val cacheDataSource = ChannelsCacheDataSource.Base(RealmProvider.Base(), ChannelDataToDbMapper.Base())
        val toBookMapper = ToChannelMapper.Base()
        val channelsCloudMapper = ChannelsCloudMapper.Base(toBookMapper)
        val channelsCacheMapper = ChannelsCacheMapper.Base(toBookMapper)
//            val guidesCloudDataSpource = GuidesCloudDataSource.Base(gson = gson, service = guidesService)
        val channelsRepository = ChannelsRepository.Base(
            cloudDataSource,
            cacheDataSource,
            channelsCloudMapper,
            channelsCacheMapper,
            settings
        )
        val programmCacheDataSource = ProgrammsCacheDataSource.Base(RealmProvider.Base(), ProgrammDataToDbMapper.Base())
        val programmsCacheMapper = ProgrammsCacheMapper.Base(ToProgrammMapper.Base())
        val programmsCloudMapper = ProgrammsCloudMapper.Base(ToProgrammMapper.Base())
        val programmCloudDataSource = ProgrammsCloudDataSource.Base(gson = Gson(), service = guidesService)
        val programmRepository = ProgrammsRepository.Base(programmCacheDataSource,
        programmCloudDataSource,
        programmsCloudMapper,
        programmsCacheMapper)

        val channelsInterceptor = ChannelsInteractor.Base(channelsRepository = channelsRepository,
        mapper = BaseChannelsDataToDomainMapper(BaseChannelDataToDomainMapper()),
            programmRepository,
            mapperGuide = BaseProgrammsDataToDomainMapper(BaseProgrammDataToDomainMapper())
        )

        val communication = ChannelsCommunication.Base()
        val programmsCommunication = ProgrammsCommunication.Base()


        mainViewModel = MainViewModel(
            channelsInterceptor,
            BaseChannelsDomainToUiMapper(ResourceProvider.Base(this),
            BaseChannelDomainToUiMapper()),

            BaseProgrammsDomainToUiMapper(ResourceProvider.Base(this),
                BaseProgrammDomainToUiMapper()),

            communication,
            programmsCommunication)
    }

    companion object{
//        lateinit var simpleCache: SimpleCache
//        const val exoPlayerCacheSize: Long = 20 * 1024 * 1024
//        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
//        lateinit var exoDatabaseProvider: StandaloneDatabaseProvider

        const val BASE_URL = "https://iptv.matrixhome.net"
    }
}