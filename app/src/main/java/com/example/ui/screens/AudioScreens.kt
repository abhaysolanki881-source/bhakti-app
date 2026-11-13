package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import com.example.ui.components.NativeAdCard
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VoiceOverOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioPlayerState
import com.example.audio.RepeatMode
import com.example.data.model.AudioTrack
import com.example.data.model.ContentCategory
import com.example.ui.components.DiyaIcon
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SunsetOrange
import com.example.ui.strings.AppStrings
import com.example.ui.strings.LocalAppLanguage
import java.util.Locale

@Composable
fun AudioLibraryScreen(
    tracks: List<AudioTrack>,
    favoriteIds: Set<String>,
    onSelectTrack: (AudioTrack) -> Unit = {},
    onPlayTrack: (AudioTrack) -> Unit,
    onToggleFavorite: (AudioTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categories = remember(lang) {
        listOf(
            AppStrings.catAll(lang),
            AppStrings.catAarti(lang),
            AppStrings.catBhajan(lang),
            AppStrings.catMantra(lang),
            AppStrings.catDhun(lang),
            AppStrings.getCategoryTitle("CHALISA", lang)
        )
    }

    val filteredTracks = remember(selectedCategoryIndex, tracks) {
        when (selectedCategoryIndex) {
            1 -> tracks.filter { it.category == ContentCategory.AARTI }
            2 -> tracks.filter { it.category == ContentCategory.BHAJAN }
            3 -> tracks.filter { it.category == ContentCategory.MANTRA }
            4 -> tracks.filter { it.category == ContentCategory.DHUN }
            5 -> tracks.filter { it.category == ContentCategory.CHALISA }
            else -> tracks
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("audio_library_screen")
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                    color = SaffronPrimary
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategoryIndex == index) SaffronDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(filteredTracks, key = { _, track -> track.id }) { index, track ->
                val isFav = favoriteIds.contains(track.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectTrack(track) }
                        .shadow(3.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Deity Artwork Thumbnail with Play Badge (Explicit Play click starts playback)
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, SaffronPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .shadow(4.dp, RoundedCornerShape(12.dp))
                                .clickable { onPlayTrack(track) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (track.imageResId != null) {
                                Image(
                                    painter = painterResource(id = track.imageResId),
                                    contentDescription = track.titleGujarati,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                // Dark scrim for contrast
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.28f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(SaffronPrimary.copy(alpha = 0.9f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = Color(0xFF1F1206),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFFF59E0B), Color(0xFFFF6D00))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = track.titleGujarati,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${track.deityNameGujarati} • ${AppStrings.getCategoryTitle(track.category.name, lang)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SaffronDark,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(1.dp, SaffronPrimary.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (lang == "hi") "🎙️ पारंपरिक गायन" else if (lang == "en") "🎙️ Traditional Vocal" else "🎙️ અસલ માનવ કંઠ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SaffronPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (track.isBonus) {
                                    val isUnlocked = com.example.services.AdMobService.isTrackUnlocked(context, track.id)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isUnlocked) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
                                            .border(1.dp, if (isUnlocked) Color(0xFF059669) else SaffronPrimary, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isUnlocked) AppStrings.unlockedBadge(lang) else AppStrings.bonusBadge(lang),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isUnlocked) Color(0xFF047857) else SaffronDark,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = AppStrings.divineMusicBadge(lang),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SaffronDark,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        IconButton(onClick = { onToggleFavorite(track) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = AppStrings.favoritesTitle(lang),
                                tint = if (isFav) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                if ((index + 1) % 5 == 0) {
                    com.example.ui.components.InlineNativeAd(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }

        com.example.ui.components.BannerAdView(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}

@Composable
fun FullPlayerScreen(
    playerState: AudioPlayerState,
    isFavorite: Boolean,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleShuffle: () -> Unit,
    onSetSleepTimer: (Int?) -> Unit,
    onToggleFavorite: () -> Unit,
    onClose: () -> Unit,
    onJumpToLine: (Int) -> Unit = {},
    onSetSpeechRate: (Float) -> Unit = {},
    onToggleVoiceEnabled: () -> Unit = {},
    onToggleBellAmbiance: () -> Unit = {},
    onToggleVocalMode: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val track = playerState.currentTrack ?: return
    val context = LocalContext.current
    val lang = LocalAppLanguage.current
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Automatically scroll to follow the currently reciting verse
    LaunchedEffect(playerState.currentLineIndex) {
        if (playerState.lines.isNotEmpty()) {
            val safeIndex = playerState.currentLineIndex.coerceIn(0, playerState.lines.size - 1)
            listState.animateScrollToItem(safeIndex)
        }
    }

    val playInteractionSource = remember { MutableInteractionSource() }
    val isPlayPressed by playInteractionSource.collectIsPressedAsState()
    val playScale by animateFloatAsState(
        targetValue = if (isPlayPressed) 0.88f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "PlayBtnScale"
    )

    if (showSleepTimerDialog) {
        SleepTimerDialog(
            currentMinutes = playerState.sleepTimerMinutesRemaining,
            onSelectMinutes = {
                onSetSleepTimer(it)
                showSleepTimerDialog = false
            },
            onDismiss = { showSleepTimerDialog = false }
        )
    }

    if (showSpeedDialog) {
        SpeedDialog(
            currentSpeed = playerState.speechRate,
            onSelectSpeed = {
                onSetSpeechRate(it)
                showSpeedDialog = false
            },
            onDismiss = { showSpeedDialog = false }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0C0805))
    ) {
        // Realistic Sacred Deity Artwork Background with Scrim
        val bgImageRes = track.imageResId ?: R.drawable.img_darshan_krishna_1793948267799
        Image(
            painter = painterResource(id = bgImageRes),
            contentDescription = track.deityNameGujarati,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Frosted dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xDD0C0805),
                            Color(0xF20C0805),
                            Color(0xFA0C0805)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = AppStrings.back(lang), tint = Color.White)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = Color(0xFFFDE68A),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.divineRecitationHeader(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFDE68A)
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = AppStrings.favoritesTitle(lang),
                            tint = if (isFavorite) Color(0xFFEF4444) else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title and Deity Header
            Text(
                text = track.titleGujarati,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFFFFFBEB),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${track.deityNameGujarati} • ${track.category.titleGujarati}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFDE68A),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Synchronized Lyrics Reader Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E140C).copy(alpha = 0.90f)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFDE68A).copy(alpha = 0.5f), Color.Transparent)
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    // Devotional Vocal & Chanting Control Strip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Vocal Mode Switcher (Real Human Singing vs Spoken Recitation)
                        // Authentic Human Vocal Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFF59E0B).copy(alpha = 0.25f))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFFDE68A),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.humanVocalBadge(lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Voice Chanting / Mute Toggle
                            IconButton(
                                onClick = onToggleVoiceEnabled,
                                modifier = Modifier.size(32.dp)
                            ) {
                                val voiceDesc = when (lang) {
                                    "en" -> "Vocals on/off"
                                    "hi" -> "स्वर चालू/बंद"
                                    else -> "સ્વર ચાલુ/બંધ"
                                }
                                Icon(
                                    imageVector = if (playerState.isVoiceEnabled) Icons.Default.RecordVoiceOver else Icons.Default.VoiceOverOff,
                                    contentDescription = voiceDesc,
                                    tint = if (playerState.isVoiceEnabled) Color(0xFFFDE68A) else Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Temple Bell Ambiance Toggle
                            IconButton(
                                onClick = onToggleBellAmbiance,
                                modifier = Modifier.size(32.dp)
                            ) {
                                val bellDesc = when (lang) {
                                    "en" -> "Temple Bell"
                                    "hi" -> "घंटनाद"
                                    else -> "ઘંટનાદ"
                                }
                                Icon(
                                    imageVector = if (playerState.isBellAmbianceEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                    contentDescription = bellDesc,
                                    tint = if (playerState.isBellAmbianceEnabled) Color(0xFFF59E0B) else Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Speed Selector
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .clickable { showSpeedDialog = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1fx", playerState.speechRate),
                                    color = Color(0xFFFDE68A),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Synchronized Line-by-Line Verses
                    val lines = if (playerState.lines.isNotEmpty()) playerState.lines else listOf(track.lyricsGujarati)

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        itemsIndexed(lines) { index, lineText ->
                            val isActive = (index == playerState.currentLineIndex)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .then(
                                        if (isActive) {
                                            Modifier.background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFFB45309).copy(alpha = 0.5f), Color(0xFFD97706).copy(alpha = 0.3f))
                                                )
                                            )
                                        } else {
                                            Modifier.background(Color.White.copy(alpha = 0.04f))
                                        }
                                    )
                                    .border(
                                        width = if (isActive) 1.5.dp else 0.5.dp,
                                        color = if (isActive) Color(0xFFFDE68A) else Color.White.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onJumpToLine(index) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isActive) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color(0xFFFDE68A),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    } else {
                                        Text(
                                            text = "${index + 1}.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.35f),
                                            fontSize = 11.sp,
                                            modifier = Modifier.width(22.dp)
                                        )
                                    }

                                    Text(
                                        text = lineText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontSize = if (isActive) 17.sp else 15.sp,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isActive) Color(0xFFFFFBEB) else Color(0xFFD1B79E),
                                        lineHeight = 24.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Sacred Meaning Footer
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1C10).copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "${AppStrings.meaningPrefix(lang)}:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFDE68A)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = track.meaningGujarati,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFE5E7EB),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Seek Bar
            Slider(
                value = playerState.currentPositionSeconds.toFloat(),
                onValueChange = { onSeekTo(it.toInt()) },
                valueRange = 0f..playerState.durationSeconds.toFloat().coerceAtLeast(1f),
                colors = SliderDefaults.colors(
                    thumbColor = SaffronPrimary,
                    activeTrackColor = SaffronPrimary,
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(playerState.currentPositionSeconds),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFD1B79E)
                )
                Text(
                    text = formatTime(playerState.durationSeconds),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFD1B79E)
                )
            }

            // Modern Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleShuffle) {
                    val shuffleDesc = when (lang) {
                        "en" -> "Shuffle"
                        "hi" -> "शफल"
                        else -> "શફલ"
                    }
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = shuffleDesc,
                        tint = if (playerState.isShuffle) SaffronPrimary else Color(0xFF9CA3AF)
                    )
                }

                IconButton(onClick = onPlayPrevious, modifier = Modifier.size(46.dp)) {
                    val prevDesc = when (lang) {
                        "en" -> "Previous"
                        "hi" -> "पिछला"
                        else -> "પાછલું"
                    }
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = prevDesc,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Replay Current Line
                IconButton(
                    onClick = { onJumpToLine(playerState.currentLineIndex) },
                    modifier = Modifier.size(42.dp)
                ) {
                    val replayDesc = when (lang) {
                        "en" -> "Replay Line"
                        "hi" -> "पंक्ति रीप्ले"
                        else -> "પંક્તિ રીપ્લે"
                    }
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = replayDesc,
                        tint = Color(0xFFFDE68A),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Animated Play Button with Spring Scale
                Box(
                    modifier = Modifier
                        .scale(playScale)
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(SaffronPrimary, SunsetOrange)
                            )
                        )
                        .shadow(16.dp, CircleShape)
                        .clickable(interactionSource = playInteractionSource, indication = null) {
                            onTogglePlayPause()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val playPauseDesc = if (playerState.isPlaying) {
                        when (lang) {
                            "en" -> "Pause"
                            "hi" -> "रोकें"
                            else -> "થોભો"
                        }
                    } else {
                        when (lang) {
                            "en" -> "Play"
                            "hi" -> "चलाएं"
                            else -> "રમો"
                        }
                    }
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = playPauseDesc,
                        tint = Color(0xFF1F1206),
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onPlayNext, modifier = Modifier.size(46.dp)) {
                    val nextDesc = when (lang) {
                        "en" -> "Next"
                        "hi" -> "अगला"
                        else -> "આગળનું"
                    }
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = nextDesc,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = onToggleRepeat) {
                    val repeatDesc = when (lang) {
                        "en" -> "Repeat"
                        "hi" -> "दोहराएं"
                        else -> "પુનરાવર્તન"
                    }
                    Icon(
                        imageVector = when (playerState.repeatMode) {
                            RepeatMode.ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = repeatDesc,
                        tint = if (playerState.repeatMode != RepeatMode.OFF) SaffronPrimary else Color(0xFF9CA3AF)
                    )
                }
            }

            // Sleep Timer & Share Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickable { showSleepTimerDialog = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bedtime, contentDescription = null, modifier = Modifier.size(16.dp), tint = SaffronPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        val timerLabel = if (playerState.sleepTimerMinutesRemaining != null) {
                            when (lang) {
                                "en" -> "${playerState.sleepTimerMinutesRemaining}m left"
                                "hi" -> "${playerState.sleepTimerMinutesRemaining} मिनट शेष"
                                else -> "${playerState.sleepTimerMinutesRemaining} મિનિટ બાકી"
                            }
                        } else {
                            when (lang) {
                                "en" -> "Sleep Timer"
                                "hi" -> "स्लीप टाइमर"
                                else -> "સ્લીપ ટાઈમર"
                            }
                        }
                        Text(
                            text = timerLabel,
                            color = Color(0xFFFDE68A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickable {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${track.titleGujarati}\n\n${track.lyricsGujarati}\n\n- ભક્તિ સાગર ગુજરાતી")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, AppStrings.share(lang)))
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = SaffronPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.share(lang), color = Color(0xFFFDE68A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedDialog(
    currentSpeed: Float,
    onSelectSpeed: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val speeds = when (lang) {
        "hi" -> listOf(
            0.75f to "0.75x (धीमा / ध्यान पूर्वक)",
            0.88f to "0.9x (शांत भक्ति भाव - अनुशंसित)",
            1.0f to "1.0x (सामान्य गति)",
            1.2f to "1.2x (तीव्र गति)"
        )
        "en" -> listOf(
            0.75f to "0.75x (Slow / Meditative)",
            0.88f to "0.9x (Peaceful Devotion - Recommended)",
            1.0f to "1.0x (Normal Speed)",
            1.2f to "1.2x (Fast Recitation)"
        )
        else -> listOf(
            0.75f to "૦.૭૫x (ધ્યાન પૂર્વક / ધીમું)",
            0.88f to "૦.૯x (શાંત ભક્તિ ભાવ - ભલામણ)",
            1.0f to "૧.૦x (સામાન્ય ગતિ)",
            1.2f to "૧.૨x (ઝડપી પઠન)"
        )
    }

    val dialogTitle = when (lang) {
        "hi" -> "वाणी पठन गति (Speed)"
        "en" -> "Recitation Speed"
        else -> "વાણી પઠન ગતિ (Speed)"
    }
    val dialogInstruction = when (lang) {
        "hi" -> "आरती और मंत्र पठन की गति चुनें:"
        "en" -> "Select recitation playback speed:"
        else -> "આરતી અને મંત્ર પઠનની ગતિ પસંદ કરો:"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Speed, contentDescription = null, tint = SaffronPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(dialogTitle, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(dialogInstruction)
                Spacer(modifier = Modifier.height(10.dp))
                speeds.forEach { (rate, label) ->
                    val isSelected = kotlin.math.abs(currentSpeed - rate) < 0.05f
                    Button(
                        onClick = { onSelectSpeed(rate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSelected) Color(0xFF1F1206) else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.close(lang))
            }
        }
    )
}

@Composable
fun SleepTimerDialog(
    currentMinutes: Int?,
    onSelectMinutes: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val options = listOf(10, 15, 30, 45, 60)
    val titleText = when (lang) {
        "hi" -> "स्लीप टाइमर सेट करें"
        "en" -> "Set Sleep Timer"
        else -> "સ્લીપ ટાઈમર સેટ કરો"
    }
    val instructionText = when (lang) {
        "hi" -> "निर्धारित समय के बाद भजन / मंत्र स्वतः बंद हो जाएगा:"
        "en" -> "Audio will automatically turn off after:"
        else -> "નિર્ધારિત સમય બાદ ભજન / મંત્ર આપમેળે બંધ થઈ જશે:"
    }
    val cancelTimerText = when (lang) {
        "hi" -> "टाइमर बंद करें"
        "en" -> "Turn Off Timer"
        else -> "ટાઈમર બંધ કરો"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titleText, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(instructionText)
                Spacer(modifier = Modifier.height(10.dp))
                options.forEach { mins ->
                    val minsLabel = when (lang) {
                        "hi" -> "$mins मिनट"
                        "en" -> "$mins minutes"
                        else -> "$mins મિનિટ"
                    }
                    Button(
                        onClick = { onSelectMinutes(mins) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentMinutes == mins) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (currentMinutes == mins) Color(0xFF1F1206) else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(minsLabel, fontWeight = FontWeight.Bold)
                    }
                }
                if (currentMinutes != null) {
                    TextButton(
                        onClick = { onSelectMinutes(null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(cancelTimerText, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.cancel(lang))
            }
        }
    )
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", m, s)
}
