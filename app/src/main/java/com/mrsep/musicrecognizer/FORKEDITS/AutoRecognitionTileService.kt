package com.mrsep.musicrecognizer.forkedits

import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.mrsep.musicrecognizer.core.domain.preferences.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.mrsep.musicrecognizer.core.strings.R as StringsR
import com.mrsep.musicrecognizer.core.ui.R as UiR

private const val TAG = "AutoRecognitionTileService"

@AndroidEntryPoint
class AutoRecognitionTileService : TileService() {

    private var coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var tileUpdateJob: Job? = null

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        requestListeningState(this)
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        tileUpdateJob?.cancel()
        tileUpdateJob = null
    }

    override fun onClick() {
        super.onClick()
        val isRunning = AutoRecognitionService.isRunning(applicationContext)

        if (isRunning) {
            // Stop the service
            AutoRecognitionService.stop(applicationContext)
            coroutineScope.launch {
                preferencesRepository.setAutoRecognizeEnabled(false)
            }
        } else {
            // Start the service
            AutoRecognitionService.start(applicationContext)
            coroutineScope.launch {
                preferencesRepository.setAutoRecognizeEnabled(true)
            }
        }

        // Update the tile after a short delay to reflect the new state
        tileUpdateJob = coroutineScope.launch {
            kotlinx.coroutines.delay(500)
            updateTile()
        }
    }

    private fun updateTile() {
        val tile = qsTile ?: run {
            Log.e(TAG, "qsTile is null, is update called in valid state?")
            return
        }

        val isRunning = AutoRecognitionService.isRunning(applicationContext)

        tile.label = getString(
            if (isRunning) {
                StringsR.string.auto_recognize_tile_active
            } else {
                StringsR.string.auto_recognize_tile_inactive
            }
        )
        tile.state = if (isRunning) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.icon = Icon.createWithResource(this, UiR.drawable.ic_auto_recognize)
        tile.updateTile()
    }

    companion object {
        fun requestListeningState(context: Context) {
            try {
                requestListeningState(
                    context.applicationContext,
                    ComponentName(context.applicationContext, AutoRecognitionTileService::class.java)
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to request qsTile listening state", e)
            }
        }
    }
}
