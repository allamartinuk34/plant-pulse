package com.plntpls.appssxdc.data.local

import android.content.Context
import android.content.SharedPreferences

class PlantPrefs(context: Context) {

    private val store: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun isSeeded(): Boolean = store.getBoolean(KEY_SEEDED, false)

    fun markSeeded() {
        store.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    fun readPlants(): String = store.getString(KEY_PLANTS, "").orEmpty()

    fun writePlants(packed: String) {
        store.edit().putString(KEY_PLANTS, packed).apply()
    }

    fun readLog(): String = store.getString(KEY_LOG, "").orEmpty()

    fun writeLog(packed: String) {
        store.edit().putString(KEY_LOG, packed).apply()
    }

    fun readStreak(): Int = store.getInt(KEY_STREAK, 0)

    fun readLastCareDay(): Int = store.getInt(KEY_LAST_CARE, 0)

    fun writeStreak(streakDays: Int, lastCareDay: Int) {
        store.edit().putInt(KEY_STREAK, streakDays).putInt(KEY_LAST_CARE, lastCareDay).apply()
    }

    fun readUnits(): String = store.getString(KEY_UNITS, "").orEmpty()

    fun readWeekStart(): String = store.getString(KEY_WEEK_START, "").orEmpty()

    fun readIntervalNudge(): Int = store.getInt(KEY_NUDGE, 0)

    fun writeSettings(units: String, weekStart: String, intervalNudge: Int) {
        store.edit()
            .putString(KEY_UNITS, units)
            .putString(KEY_WEEK_START, weekStart)
            .putInt(KEY_NUDGE, intervalNudge)
            .apply()
    }

    fun clear() {
        store.edit().clear().apply()
    }

    private companion object {
        const val STORE_NAME = "plantpulse_store"
        const val KEY_SEEDED = "seeded"
        const val KEY_PLANTS = "plants_v1"
        const val KEY_LOG = "log_v1"
        const val KEY_STREAK = "streak_days"
        const val KEY_LAST_CARE = "last_care_day"
        const val KEY_UNITS = "pref_units"
        const val KEY_WEEK_START = "pref_week_start"
        const val KEY_NUDGE = "pref_interval_nudge"
    }
}
