package net.matrixhome.matrixiptv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InitViewModel: ViewModel() {


    var lastAppVersion = MutableLiveData<Int>()
}