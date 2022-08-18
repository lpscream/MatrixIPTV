package net.matrixhome.matrixiptv.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.matrixhome.matrixiptv.viewmodel.ChannelsListViewModel
import java.lang.IllegalArgumentException

class ChannelsListViewModelFactory(
    private val application: Application,

    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChannelsListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}