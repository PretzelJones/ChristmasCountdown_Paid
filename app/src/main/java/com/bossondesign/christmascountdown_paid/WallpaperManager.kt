package com.bossondesign.christmascountdown_paid

import android.content.Context
import android.content.SharedPreferences
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class WallpaperManager(private val context: Context, private val layoutId: Int) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("WallpaperPreference", Context.MODE_PRIVATE)

    fun changeWallpaper(wallpaperId: String) {
        val wallpaperResId = when (wallpaperId) {
            "red_wall" -> R.drawable.red
            "green_wall" -> R.drawable.green
            "blue_wall" -> R.drawable.blue
            "peach_wall" -> R.drawable.peach
            "black_wall" -> R.drawable.black
            "grey_wall" -> R.drawable.grey
            else -> R.drawable.red // Your default wallpaper
        }

        val layout = (context as AppCompatActivity).findViewById<RelativeLayout>(layoutId)
        layout.setBackgroundResource(wallpaperResId)

        saveWallpaperPreference(wallpaperId)
    }

    private fun saveWallpaperPreference(wallpaperId: String) {
        sharedPrefs.edit().putString("selectedWallpaper", wallpaperId).apply()
    }

    fun loadWallpaperPreference() {
        val wallpaperId = sharedPrefs.getString("selectedWallpaper", "default_wallpaper") // Default wallpaper ID
        changeWallpaper(wallpaperId ?: "default_wallpaper")
    }
}
