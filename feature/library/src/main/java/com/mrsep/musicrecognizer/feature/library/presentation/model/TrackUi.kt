package com.mrsep.musicrecognizer.feature.library.presentation.model

import com.mrsep.musicrecognizer.core.common.util.AppDateTimeFormatter
import com.mrsep.musicrecognizer.core.domain.track.model.TrackPreview
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.annotation.concurrent.Immutable

@Immutable
internal data class TrackUi(
    val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val artworkThumbUrl: String?,
    val recognitionDate: String,
    val timestampFormat: String,
    val isViewed: Boolean,
)

private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

internal fun TrackPreview.toUi(dateTimeFormatter: AppDateTimeFormatter) = TrackUi(
    id = id,
    title = title,
    artist = artist,
    album = album,
    artworkThumbUrl = artworkThumbUrl,
    recognitionDate = dateTimeFormatter.formatRelativeToToday(
        recognitionDate.atZone(ZoneId.systemDefault())
    ),
    timestampFormat = recognitionDate.atZone(ZoneId.systemDefault()).format(timestampFormatter),
    isViewed = isViewed,
)
