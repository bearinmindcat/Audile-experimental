package com.mrsep.musicrecognizer.forkedits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrsep.musicrecognizer.core.domain.preferences.AudioCaptureMode
import com.mrsep.musicrecognizer.core.domain.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForkEditsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val preferencesFlow = preferencesRepository.userPreferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setUseAltDeviceSoundSource(value: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setUseAltDeviceSoundSource(value)

            // When turning OFF Visualizer API, reset any Visualizer-specific modes to defaults
            if (!value) {
                val currentPrefs = preferencesRepository.userPreferencesFlow.first()

                // Reset default mode to Microphone if it was Visualizer or AutoVisualizerMic
                if (currentPrefs.defaultAudioCaptureMode == AudioCaptureMode.Visualizer ||
                    currentPrefs.defaultAudioCaptureMode == AudioCaptureMode.AutoVisualizerMic ||
                    currentPrefs.defaultAudioCaptureMode == AudioCaptureMode.AutoDeviceVisualizer) {
                    preferencesRepository.setDefaultAudioCaptureMode(AudioCaptureMode.Microphone)
                }

                // Reset button long press mode to Auto if it was Visualizer or AutoVisualizerMic
                if (currentPrefs.mainButtonLongPressAudioCaptureMode == AudioCaptureMode.Visualizer ||
                    currentPrefs.mainButtonLongPressAudioCaptureMode == AudioCaptureMode.AutoVisualizerMic ||
                    currentPrefs.mainButtonLongPressAudioCaptureMode == AudioCaptureMode.AutoDeviceVisualizer) {
                    preferencesRepository.setMainButtonLongPressAudioCaptureMode(AudioCaptureMode.Auto)
                }
            }
        }
    }

    fun setShowTimestampFormatInLibrary(value: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setShowTimestampFormatInLibrary(value)
        }
    }

    fun setShowRecognitionPopup(value: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setShowRecognitionPopup(value)
        }
    }

    fun setAutoRecognizeEnabled(value: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setAutoRecognizeEnabled(value)
        }
    }
}
