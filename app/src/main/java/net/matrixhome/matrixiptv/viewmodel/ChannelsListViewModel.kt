package net.matrixhome.matrixiptv.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import net.matrixhome.matrixiptv.players.HlsExoPlayerBuilder
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI

class ChannelsListViewModel(application: Application): AndroidViewModel(application),
    LifecycleObserver {
    private val TAG = "ChannelsListViewMdl_log"
    private val _player = MutableLiveData<ExoPlayer?>()
    val player: LiveData<ExoPlayer?> get() = _player
    private lateinit var hlsPlayerBuilder: HlsExoPlayerBuilder
    private lateinit var list: List<ChannelUI>
    private var channelNum: Int = 0
    private var channelListScrollState = MutableLiveData<Int>()

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        channelListScrollState.value = 0
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForegrounded() {
        initPlayer(list)
        seekTo(channelNum)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        //fixme 12.07.2022 надо решить чтобы при перевороте не прерывалось
        //fixme может быть прикрутить кеш для видео)))
        Log.d(TAG, "onBackgrounded: ")
        player.value!!.stop()
    //releasePlayer()
    }

    fun initPlayer(channelList: List<ChannelUI>){
        list = channelList
        releasePlayer()
        hlsPlayerBuilder = HlsExoPlayerBuilder(getApplication())
        _player.value = hlsPlayerBuilder.build(channelList)
    }

    fun seekTo(position: Int){
        if (position <= player.value!!.mediaItemCount){
            channelNum = position
            player.value!!.seekTo(position, 0)
            player.value!!.play()
        }else{
            player.value!!.seekTo(0,0)
            player.value!!.play()
        }
    }

    private fun releasePlayer(){
        if (player.value == null) {
            return
        }
        player.value?.release()
    }


    fun setChannelListState(state: Int){
       channelListScrollState.postValue(state)
    }
    fun observeChannelListScroll(owner: LifecycleOwner, observer: Observer<Int>){
        channelListScrollState.observe(owner, observer)
    }

}