package net.matrixhome.matrixiptv.players

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI

class HlsExoPlayerBuilder(private val context: Context) {

    private val TAG = "HlsExoPlayerBuilder"
    private val renderersFactory: RenderersFactory =
        DefaultRenderersFactory(context)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)



    fun build(channelList: List<ChannelUI>): ExoPlayer{
        val player = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            .build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()
        player.setAudioAttributes(audioAttributes, false)
        val mediaItems: MutableList<Any> = mutableListOf()

        when(channelList.get(0)){
            is ChannelUI.Base -> {
                player.clearMediaItems()
                channelList.forEach {
                    it.map(object : ChannelUI.StringMapper{
                        override fun map(id: Int, name: String, url: String, number: Int) {
                            mediaItems.add(buildMedia(url, id, number))
                        }
                    })
                }
            }
            else -> player.stop()
        }

        player.setMediaSources(mediaItems as List<MediaSource>)

        //player.playWhenReady = playWhenReady
        //player.seekTo(currentWindow, C.TIME_UNSET)
        //player.prepare()
        //player.play()
        //todo add playerview in observer
        //todo add listener in observer
        return player
    }




    private fun buildMedia(url: String,id: Int, number: Int ): HlsMediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        var hlsMediaSource: HlsMediaSource = HlsMediaSource
            .Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(
                MediaItem.Builder()
                    .setUri(url)
                    .setTag(number.toString())
                    .setMediaId(id.toString())
                    //.setMediaMetadata(object : MediaMetadata.Builder)
                    //.setMimeType(MimeTypes.BASE_TYPE_APPLICATION)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build())
        return hlsMediaSource
    }

}