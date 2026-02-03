package com.mrsep.musicrecognizer.forkedits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mrsep.musicrecognizer.core.ui.components.preferences.PreferenceGroup
import com.mrsep.musicrecognizer.core.ui.components.preferences.PreferenceSwitchItem

object ForkEditsScreen {
    const val ROUTE = "fork_edits"
}

fun NavGraphBuilder.forkEditsScreen() {
    composable(ForkEditsScreen.ROUTE) {
        ForkEditsScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForkEditsScreenContent(
    viewModel: ForkEditsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferencesFlow.collectAsStateWithLifecycle()
    val topBarBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("FORK EDITS") },
            scrollBehavior = topBarBehaviour
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topBarBehaviour.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
        ) {
            PreferenceGroup(title = "Experimental fork features") {
                preferences?.let { prefs ->
                    PreferenceSwitchItem(
                        title = "Visualizer API",
                        subtitle = "Uses waveform to determine song",
                        checked = prefs.useAltDeviceSoundSource,
                        onClick = {
                            viewModel.setUseAltDeviceSoundSource(!prefs.useAltDeviceSoundSource)
                        }
                    )

                    PreferenceSwitchItem(
                        title = "Timestamp",
                        subtitle = "Displays timestamp in library",
                        checked = prefs.showTimestampFormatInLibrary,
                        onClick = {
                            viewModel.setShowTimestampFormatInLibrary(!prefs.showTimestampFormatInLibrary)
                        }
                    )

                    PreferenceSwitchItem(
                        title = "Permanent recognition pop-up",
                        subtitle = "Recognized songs appear in notification bar",
                        checked = prefs.showRecognitionPopup,
                        onClick = {
                            viewModel.setShowRecognitionPopup(!prefs.showRecognitionPopup)
                        }
                    )

                    // Auto recognize - always enabled, controlled via quick settings
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Auto recognize song",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Icon in quick settings, high usage",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "ALWAYS ENABLED",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
