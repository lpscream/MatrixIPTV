package net.matrixhome.matrixiptv.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.matrixhome.matrixiptv.domain.channels.ChannelsDomainToUiMapper
import net.matrixhome.matrixiptv.domain.channels.ChannelsInteractor
import net.matrixhome.matrixiptv.domain.programms.ProgrammsDomainToUiMapper
import net.matrixhome.matrixiptv.presentation.channels.ChannelUI
import net.matrixhome.matrixiptv.presentation.channels.ChannelsCommunication
import net.matrixhome.matrixiptv.presentation.programms.ProgrammUI
import net.matrixhome.matrixiptv.presentation.programms.ProgrammsCommunication

class MainViewModel(
    private val channelsInteractor: ChannelsInteractor,
    private val mapper: ChannelsDomainToUiMapper,
    private val programmMapper: ProgrammsDomainToUiMapper,
    private val communication: ChannelsCommunication,
    private val programmsCommunication: ProgrammsCommunication
) : ViewModel() {

    init {
        programmsCommunication.map(ProgrammsSkeletonArray().build())
    }

    fun fetchChannels() {
        communication.map(ChannelsSkeletonArray().build())
        viewModelScope.launch(Dispatchers.IO) {
            val resultDomain = channelsInteractor.fetchChannels()
            val resultUi = resultDomain.map(mapper)
            withContext(Dispatchers.Main) {
                resultUi.map(communication)
            }
        }
    }

    fun fetchSortedChannels(category: String){
        communication.map(ChannelsSkeletonArray().build())
        viewModelScope.launch(Dispatchers.IO) {
            val resultDomain = channelsInteractor.fetchSortedChannels(category)
            val resultUi = resultDomain.map(mapper)
            withContext(Dispatchers.Main) {
                resultUi.map(communication)
            }
        }
    }


    fun fetchProgramms(id: String){
        programmsCommunication.map(ProgrammsSkeletonArray().build())
        viewModelScope.launch(Dispatchers.IO) {
            val resultDomain = channelsInteractor.fetchProgramms(id)
            val resulyUi = resultDomain.map(programmMapper)
            withContext(Dispatchers.Main){
              //programmsCommunication.map(ProgrammsSkeletonArray().build())
            resulyUi.map(programmsCommunication)
            }
        }
    }

    fun observeChannelUI(owner: LifecycleOwner, observer: Observer<List<ChannelUI>>) {
        communication.observe(owner, observer)
    }

    fun observeProgramms(owner: LifecycleOwner, observer: Observer<List<ProgrammUI>>){
        programmsCommunication.observe(owner, observer)
    }
}

class ChannelsSkeletonArray(){
    fun build(): ArrayList<ChannelUI.Progress>{
        val array = arrayListOf<ChannelUI.Progress>()
        for (i in 0..40){
            array.add(ChannelUI.Progress)
        }
        return array
    }
}

class ProgrammsSkeletonArray(){
    fun build(): ArrayList<ProgrammUI.Progress>{
        val arrayList = arrayListOf<ProgrammUI.Progress>()
        for (i in 0..40){
            arrayList.add(ProgrammUI.Progress)
        }
        return arrayList
    }
}