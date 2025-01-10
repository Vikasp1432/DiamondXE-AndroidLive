package com.diamondxe.Activity.SharedData

import android.content.Context
import android.content.SharedPreferences

/*class SharedPreferenceHelper(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_APP_UPDATE_SHOWN = "key_app_update_shown"
    }

    fun setAppUpdateDialogShown(isShown: Boolean) {
        preferences.edit().putBoolean(KEY_APP_UPDATE_SHOWN, isShown).apply()
    }

    fun isAppUpdateDialogShown(): Boolean {
        return preferences.getBoolean(KEY_APP_UPDATE_SHOWN, false)
    }

    fun resetAppUpdateDialogFlag() {
        preferences.edit().remove(KEY_APP_UPDATE_SHOWN).apply()
    }
}*/

class SharedPreferenceHelper(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_APP_UPDATE_SHOWN_TIMESTAMP = "key_app_update_shown_timestamp"
        private const val MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000L
    }

    /**
     * Sets the timestamp when the app update dialog is shown.
     */
    fun setAppUpdateDialogShown() {
        val currentTimestamp = System.currentTimeMillis()
        preferences.edit().putLong(KEY_APP_UPDATE_SHOWN_TIMESTAMP, currentTimestamp).apply()
    }

    /**
     * Checks if the app update dialog should be shown.
     * The dialog should be shown if:
     * 1. It has never been shown before (timestamp doesn't exist).
     * 2. It was last shown more than 24 hours ago.
     */
    fun shouldShowAppUpdateDialog(): Boolean {
        val lastShownTimestamp = preferences.getLong(KEY_APP_UPDATE_SHOWN_TIMESTAMP, -1)
        if (lastShownTimestamp == -1L) {
            // Never shown before
            return true
        }
        val currentTimestamp = System.currentTimeMillis()
        return (currentTimestamp - lastShownTimestamp) >= MILLISECONDS_IN_A_DAY
    }
}
