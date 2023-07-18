package io.nervina.joyid.plugins.screenOrientation

import android.content.pm.ActivityInfo
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity

class ScreenOrientation(private val activity: AppCompatActivity) {
    private var configOrientation = 0
    fun getCurrentOrientationType(): String {
        val rotation = activity.windowManager.defaultDisplay.rotation
        return fromRotationToOrientationType(rotation)
    }

    fun lock(orientationType: String) {
        val orientationEnum = fromOrientationTypeToEnum(orientationType)
        activity.requestedOrientation = orientationEnum
    }

    fun unlock() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    fun hasOrientationChanged(orientation: Int): Boolean {
        return if (orientation == configOrientation) {
            false
        } else {
            configOrientation = orientation
            true
        }
    }

    private fun fromRotationToOrientationType(rotation: Int): String {
        return when (rotation) {
            Surface.ROTATION_90 -> "landscape-primary"
            Surface.ROTATION_180 -> "portrait-secondary"
            Surface.ROTATION_270 -> "landscape-secondary"
            else -> "portrait-primary"
        }
    }

    private fun fromOrientationTypeToEnum(orientationType: String): Int {
        return when (orientationType) {
            "landscape-primary" -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            "landscape-secondary" -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            "portrait-secondary" -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            else ->                 // Case: portrait-primary
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}