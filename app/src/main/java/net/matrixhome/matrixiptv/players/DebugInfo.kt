package net.matrixhome.matrixiptv.players

import android.util.Log
import android.widget.TextView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.decoder.DecoderCounters
import net.matrixhome.matrixiptv.data.DEBUG_INFO
import java.util.*

class DebugInfo(private val player: ExoPlayer, private val view: TextView) {

    private val TAG = "DebugInfo_log"

    fun updateAndPost(){
        if (DEBUG_INFO){
            view.setText(getDebugInfo())
            //Log.d(TAG, "updateAndPost: ${getDebugInfo()}")
        }
    }

    private fun getDebugInfo(): String {
        return getPlayerStateString() + getVideoString() + getAudioString()
    }

    private fun getAudioString(): String {
        var format = player.audioFormat
        var decoderCounters = player.audioDecoderCounters
        if (format == null || decoderCounters == null){
            return ""
        }
        return "\n" +
                "${format.sampleMimeType}\n" +
                "(id:" +
                "${format.id}\n" +
                "hz:" +
                "${format.sampleRate}\n" +
                "ch:" +
                "${format.channelCount}\n" +
                "${getDecoderCountersBufferCountString(decoderCounters)} " +
                ")\n"

    }

    private fun getVideoString(): String {
        var format = player.videoFormat
        var decoderCounters = player.videoDecoderCounters
        if (format == null || decoderCounters == null){
            return "video state unknown"
        }
        return "\n" +
                "${format.sampleMimeType}\n" +
                "(id: " +
                "${format.id}\n" +
                "r: " +
                "${format.width}" +
                "x" +
                "${format.height}" +
                "${getPixelAspectRatioString(format.pixelWidthHeightRatio)}\n" +
                "${getDecoderCountersBufferCountString(decoderCounters)}\n" +
                "vfpo: " +
                "${getVideoFrameProcessingOffsetAverageString(
                    decoderCounters.totalVideoFrameProcessingOffsetUs,
                    decoderCounters.videoFrameProcessingOffsetCount)}" +
                ")\n"

    }

    private fun getVideoFrameProcessingOffsetAverageString(offsetUs: Long, frameProcessingOffsetCount: Int): String {
        return if (frameProcessingOffsetCount == 0){
                 "N/A"
            }else{
                String.format(
                    Locale.US,
                    "%.02f",
                    offsetUs.toDouble()/frameProcessingOffsetCount)
            }
    }

    private fun getDecoderCountersBufferCountString(counters: DecoderCounters): String {
        if (counters == null) return ""
        counters.ensureUpdated()
        return "sib:" +
                "${counters.skippedInputBufferCount} " +
                "sb:" +
                "${counters.skippedOutputBufferCount} " +
                "rb:" +
                "${counters.renderedOutputBufferCount} " +
                "\n"+
                "db:" +
                "${counters.droppedBufferCount} " +
                "mcdb:" +
                "${counters.maxConsecutiveDroppedBufferCount} " +
                "dk:" +
                "${counters.droppedToKeyframeCount} "
    }

    private fun getPixelAspectRatioString(ratio: Float): String {
            return if (ratio == Format.NO_VALUE.toFloat() || ratio == 1f)  ""
        else {"par: ${String.format(Locale.US, "%.02f", ratio)} "}
    }

    private fun getPlayerStateString(): String {
        var playbackState: String = "STATE_UNKNOWN\n"
        when(player.playbackState){
            Player.STATE_BUFFERING -> playbackState = "STATE_BUFFERING\n"
            Player.STATE_ENDED -> playbackState = "STATE_ENDED\n"
            Player.STATE_IDLE -> playbackState = "STATE_IDLE\n"
            Player.STATE_READY -> playbackState = "STATE_READY\n"
        }
        return playbackState
    }
}