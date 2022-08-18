package net.matrixhome.matrixiptv.ui.update

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import net.matrixhome.matrixiptv.MainActivity
import net.matrixhome.matrixiptv.R

class FaultProviderMessage(context: MainActivity) {
    private var  context: MainActivity? = null
    private val TAG = "FaultProviderMMsg_log"
    init {
        this.context = context
    }
    fun build(){
        try {
            val message: AlertDialog.Builder = AlertDialog.Builder(context)
            message.setMessage(R.string.empty_list)
                .setCancelable(false)
                .setNegativeButton(R.string.exitAPP, DialogInterface.OnClickListener { dialogInterface, i ->
                    context?.finish()
                })
            val alert: AlertDialog = message.create()
            alert.show()
        }catch (e: Exception){
            Log.d(TAG, "build: ${e.message}")
        }
    }
}