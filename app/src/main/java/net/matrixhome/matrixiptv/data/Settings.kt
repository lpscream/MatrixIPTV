package net.matrixhome.matrixiptv.data

import android.content.Context
import android.util.Log

class Settings(context: Context) {
    //TODO you should do this in clean
    private val settings = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    private val editor = settings.edit()
    private val TAG = "Settings_log"

    fun fetchCategory(): Int{
            return settings.getString("category", "0")!!.toInt()
    }

    fun fetchChannelNumber(): Int{
        return settings.getString("channel_number", "0")!!.toInt()
    }

    fun fetchCategoryName(): String{
        return settings.getString("category_name", "Все")!!
    }

    fun fetchID(): String{
        return settings.getString("channel_id", "0")!!
    }

    fun saveCategory(num: Int){
        editor.putString("category", num.toString())
        editor.apply()
    }

    fun saveCategoryName(name: String){
        editor.putString("category_name", name)
        editor.apply()
    }

    fun saveChannelNumber(number: Int){
        editor.putString("channel_number", number.toString())
        editor.apply()
    }

    fun saveID(id: String){
        editor.putString("channel_id", id)
        editor.apply()
    }


    fun fetchVersionNumber(): String? {
        val data = settings.getString("LastIgnoredUpdateVersion", null)
        if (data == null) Log.d("SettingsManager", "No settings LastIgnoredUpdateVersion is stored! ") else Log.d(
            "SettingsManager",
            "Got settings LastIgnoredUpdateVersion equal to $data"
        )
        return data
    }


    fun put(value: String) {
        val editor = settings.edit()
        editor.putString("LastIgnoredUpdateVersion", value)
        Log.d("SettingsManager", "Saved setting LastIgnoredUpdateVersion equal to $value")
        editor.commit()
    }
}