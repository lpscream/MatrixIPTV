package net.matrixhome.matrixiptv.presentation.channels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.matrixhome.matrixiptv.core.Abstract

interface ChannelsCommunication : Abstract.Mapper {
    fun map(channels: List<ChannelUI>)
    fun observe(owner: LifecycleOwner, observer: Observer<List<ChannelUI>>)

    class Base : ChannelsCommunication {
        private val listLiveData = MutableLiveData<List<ChannelUI>>()
        override fun map(channels: List<ChannelUI>) {
            listLiveData.value = channels
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<List<ChannelUI>>) {
            listLiveData.observe(owner, observer)
        }
    }
}