package net.matrixhome.matrixiptv.ui

import android.view.KeyEvent

interface IOnKeyDown {
    fun OnKeyDownPressed(keyCode: Int, event: KeyEvent?)
}