package net.matrixhome.matrixiptv.presentation.programms

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.matrixhome.matrixiptv.core.Abstract

interface ProgrammsCommunication : Abstract.Mapper {
    fun map(progarmms: List<ProgrammUI>)
    fun observe(owner: LifecycleOwner, observer: Observer<List<ProgrammUI>>)


    class Base : ProgrammsCommunication {
        private val listLiveData = MutableLiveData<List<ProgrammUI>>()
        override fun map(progarmms: List<ProgrammUI>) {
            listLiveData.value = progarmms
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<List<ProgrammUI>>) {
            listLiveData.observe(owner, observer)
        }
    }
}