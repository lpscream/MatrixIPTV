package net.matrixhome.matrixiptv

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.DialogInterface
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.matrixhome.matrixiptv.data.FILE_WITH_APK_UPDATE
import net.matrixhome.matrixiptv.data.FILE_WITH_VERSION_NUMBER
import net.matrixhome.matrixiptv.ui.ChannelsListFragment
import net.matrixhome.matrixiptv.ui.IOnKeyDown
import net.matrixhome.matrixiptv.data.Settings
import net.matrixhome.matrixiptv.ui.update.BroadcastRecieverOnDowloadComplete
import net.matrixhome.matrixiptv.viewmodel.InitViewModel
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var updateStatus = true

    private lateinit var viewModel: InitViewModel
    private lateinit var settings: Settings
    private var downloadID: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(InitViewModel::class.java)
        settings = Settings(applicationContext)
        //todo check bundles saved states if needed
//        supportFragmentManager.beginTransaction()
//            .add(R.id.player_fragment_container, IPTVFragment())
//            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.channel_list_container, ChannelsListFragment())
            //.commit()
        checkUpdates()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment = this
            .supportFragmentManager
            .findFragmentById(R.id.player_fragment_container)
        (fragment as? IOnKeyDown)?.OnKeyDownPressed(keyCode, event)?.let {
            super.onKeyDown(keyCode, event)
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun checkUpdates() {
        getLastAppVersion()
        viewModel.lastAppVersion.observe(this, Observer {
            if (updateStatus) {
                if (it == 0) {
                    //Log.d(TAG, "checkUpdates: 0")
                    //startFilmCatalougeFragment()
                    return@Observer
                }
                if (it <= BuildConfig.VERSION_CODE) {
                    //Log.d(TAG, "checkUpdates: BuildConfig.VERSION_CODE ${BuildConfig.VERSION_CODE}")
                    //startFilmCatalougeFragment()
                    return@Observer
                }
                val lastVersionNumber: String? =
                    settings.fetchVersionNumber()
                if (lastVersionNumber != null) {
                    val liInt: Int = lastVersionNumber.toInt()
                    if (liInt >= viewModel.lastAppVersion.value!!) {
                        //startFilmCatalougeFragment()
                        return@Observer
                    }
                }
                updateApplication(it)
            } else {
                //startFilmCatalougeFragment()
            }
            updateStatus = false
        })
    }

    private fun getLastAppVersion() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                val url = URL(FILE_WITH_VERSION_NUMBER)
                val inStream = BufferedReader(InputStreamReader(url.openStream()))
                var str: String
                inStream.forEachLine {
                    val strNum: Int = it.indexOf("releaseVersionCode")
                    str = it.substring(strNum + ("releaseVersionCode").length).trim()
                    viewModel.lastAppVersion.postValue(str.toInt())
                }
                inStream.close()
            } catch (e: Exception) {
//                coroutineScope.launch(Dispatchers.Main) {
//                    FaultProviderMessage(this@MainActivity).build()
//                }
            }
        }
    }

    private fun updateApplication(lastAppVersion: Int) {
        //Log.d(TAG, "updateApplication: " + lastAppVersion)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.alertForUpdate)
            .setCancelable(true)
            .setPositiveButton("Да", DialogInterface.OnClickListener { dialogInterface, i ->
                //Log.d(TAG, "updateApplication: click yes button")
                val directory: File? =
                    applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val file = File(directory, "app-armeabi-v7a-release.apk")
                if (file.exists()) {
                    file.delete()
                }
                var fileUri: Uri
                if (Build.VERSION.SDK_INT < 24) {
                    fileUri = Uri.fromFile(file)
                    //Log.d(TAG, "sdk version is < 24")
                    //Log.d(TAG, "fileURi: " + fileUri.path)
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    fileUri = FileProvider.getUriForFile(
                        applicationContext,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        file
                    )
//                    Log.d(TAG, "sdk version is >= 24")
//                    Log.d(TAG, "fileURi: " + fileUri.path)
                }
                val request: DownloadManager.Request =
                    DownloadManager.Request(Uri.parse(FILE_WITH_APK_UPDATE))
                        .setTitle("Обновление Кинозал")
                        .setDescription("Загрузка")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                //Log.d(TAG, "file: " + file.toString())
                val downloadManager: DownloadManager =
                    this.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                //Log.d(TAG, "create download manager")
                downloadID = downloadManager.enqueue(request)
                //Log.d(TAG, "downloadID is " + downloadID)
                application.registerReceiver(
                    BroadcastRecieverOnDowloadComplete(
                        downloadID,
                        application
                    ), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                )
                dialogInterface.dismiss()
            })
            .setNegativeButton("Нет", DialogInterface.OnClickListener { dialogInterface, i ->
                settings.put(
                    lastAppVersion.toString()
                )
                //startFilmCatalougeFragment()
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}