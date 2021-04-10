package com.besirkaraoglu.newsapp.util

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Functions {
    companion object{
        fun formatDate(date:String):String{
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val output = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())
            return try {
                val nDate = input.parse(date)
                nDate.let { output.format(it) }
            }catch (ex: Exception){
                Log.d("time", "formatDate: ${ex.message}")
                "Bad format"
            }

        }
    }
}