package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import com.example.ads.AdMobManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BhaktiTopBar
import com.example.ui.components.MiniAudioPlayer
import com.example.ui.strings.AppStrings
import com.example.ui.screens.AudioLibraryScreen
import com.example.ui.screens.DarshanScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.FullPlayerScreen
import com.example.ui.screens.GitaReaderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JapMalaScreen
import com.example.ui.screens.MoreMenuScreen
import com.example.ui.screens.NoInternetScreen
import com.example.ui.screens.PanchangScreen
import com.example.ui.screens.ReadScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.TermsAndConditionsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TempleDirectoryScreen
import com.example.ui.screens.WallpaperStudioScreen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.model.AudioTrack
import com.example.services.AdMobService
import com.example.ui.components.BonusAudioUnlockDialog
import com.example.ui.theme.BhaktiSagarTheme
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.TempleGold
import com.example.ui.viewmodel.BhaktiViewModel
import com.example.ui.viewmodel.BottomTab
import com.example.ui.viewmodel.SubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: BhaktiViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        if (!viewModel.networkObserver.isCurrentlyConnected()) {
            viewModel.onInternetLost()
        }
        viewModel.resetHomeDarshanToToday()
        AdMobManager.onAppForegrounded(this)
    }

    override fun onPause() {
        super.onPause()
        AdMobManager.onAppBackgrounded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AdMobManager.initialize(this)
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()

            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    ) { isDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    ) { isDarkTheme }
                )
                onDispose {}
            }

            BhaktiSagarTheme(darkTheme = isDarkTheme) {
                androidx.compose.runtime.CompositionLocalProvider(
                    com.example.ui.strings.LocalAppLanguage provides currentLanguage
                ) {
                    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

                    androidx.compose.runtime.LaunchedEffect(isOnline) {
                        if (!isOnline) {
                            viewModel.onInternetLost()
                        }
                    }

                    if (!isOnline) {
                        NoInternetScreen(
                            onRetry = {
                                viewModel.retryConnection()
                            }
                        )
                    } else {
                        BhaktiAppContent(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BhaktiAppContent(viewModel: BhaktiViewModel) {
    val activity = LocalContext.current as? Activity
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeSubScreen by viewModel.activeSubScreen.collectAsStateWithLifecycle()
    val audioState by viewModel.audioPlayerState.collectAsStateWithLifecycle()
    val panchangData by viewModel.panchangState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val lifetimeRounds by viewModel.lifetimeMalaRounds.collectAsStateWithLifecycle()
    val lifetimeBeads by viewModel.lifetimeMalaBeads.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val morningReminder by viewModel.morningReminder.collectAsStateWithLifecycle()
    val eveningReminder by viewModel.eveningReminder.collectAsStateWithLifecycle()
    val selectedCalendarYear by viewModel.selectedCalendarYear.collectAsStateWithLifecycle()
    val selectedCalendarMonth0 by viewModel.selectedCalendarMonth0.collectAsStateWithLifecycle()
    val calendarDays by viewModel.calendarDays.collectAsStateWithLifecycle()
    val festivalsForYear by viewModel.festivalsForSelectedYear.collectAsStateWithLifecycle()

    val currentHomeDarshan by viewModel.currentHomeDarshan.collectAsStateWithLifecycle()
    val todayGitaShloka by viewModel.todayGitaShloka.collectAsStateWithLifecycle()
    val featuredAarti by viewModel.featuredAartiForDay.collectAsStateWithLifecycle()
    val featuredBhajan by viewModel.featuredBhajanForDay.collectAsStateWithLifecycle()
    val selectedHomeDarshanDay by viewModel.selectedHomeDarshanDay.collectAsStateWithLifecycle()
    val todaySystemDayOfWeek = viewModel.todaySystemDayOfWeek

    val favoriteIds = favorites.map { it.id }.toSet()

    LaunchedEffect(audioState.isPlaying) {
        AdMobManager.setAudioPlaying(audioState.isPlaying)
    }

    val context = LocalContext.current
    var pendingBonusTrack by remember { mutableStateOf<AudioTrack?>(null) }

    val onSelectAudioTrack: (AudioTrack) -> Unit = { track ->
        if (track.isBonus && !AdMobService.isTrackUnlocked(context, track.id)) {
            pendingBonusTrack = track
        } else {
            viewModel.selectTrack(track)
            viewModel.openSubScreen(SubScreen.FullPlayer)
        }
    }

    val onPlayAudioDirect: (AudioTrack) -> Unit = { track ->
        if (track.isBonus && !AdMobService.isTrackUnlocked(context, track.id)) {
            pendingBonusTrack = track
        } else {
            viewModel.playTrack(track)
            viewModel.openSubScreen(SubScreen.FullPlayer)
        }
    }

    // Optional Bonus Track Unlock Dialog (Standard audio remains 100% free)
    pendingBonusTrack?.let { bonusTrack ->
        BonusAudioUnlockDialog(
            trackTitle = bonusTrack.titleGujarati,
            onConfirmWatchAd = {
                val targetTrack = bonusTrack
                pendingBonusTrack = null
                AdMobManager.showBonusTrackRewardedAd(
                    activity = activity,
                    trackId = targetTrack.id,
                    trackTitle = targetTrack.titleGujarati,
                    lang = currentLanguage,
                    onUnlocked = {
                        android.widget.Toast.makeText(
                            context,
                            AppStrings.bonusAudioUnlockedSuccess(targetTrack.titleGujarati, currentLanguage),
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                        viewModel.playTrack(targetTrack)
                        viewModel.openSubScreen(SubScreen.FullPlayer)
                    },
                    onDismissedWithoutReward = { reason ->
                        android.widget.Toast.makeText(
                            context,
                            reason,
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onDismiss = {
                pendingBonusTrack = null
            }
        )
    }

    // Handle system back gesture for active sub screens
    BackHandler(enabled = activeSubScreen !is SubScreen.None) {
        viewModel.closeSubScreen()
    }

    // Sub-Screen Router
    when (val sub = activeSubScreen) {
        is SubScreen.FullPlayer -> {
            FullPlayerScreen(
                playerState = audioState,
                isFavorite = audioState.currentTrack?.let { favoriteIds.contains(it.id) } == true,
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onSeekTo = { viewModel.seekTo(it) },
                onPlayNext = { viewModel.playNext() },
                onPlayPrevious = { viewModel.playPrevious() },
                onToggleRepeat = { viewModel.toggleRepeat() },
                onToggleShuffle = { viewModel.toggleShuffle() },
                onSetSleepTimer = { viewModel.setSleepTimer(it) },
                onToggleFavorite = {
                    audioState.currentTrack?.let { track ->
                        viewModel.toggleFavorite(track, favoriteIds.contains(track.id))
                    }
                },
                onClose = { viewModel.closeSubScreen() },
                onJumpToLine = { viewModel.jumpToLine(it) },
                onSetSpeechRate = { viewModel.setSpeechRate(it) },
                onToggleVoiceEnabled = { viewModel.toggleVoiceEnabled() },
                onToggleBellAmbiance = { viewModel.toggleBellAmbiance() },
                onToggleVocalMode = { viewModel.toggleVocalMode() }
            )
        }
        is SubScreen.DeityDarshanGallery -> {
            DarshanScreen(
                darshanList = viewModel.repository.dailyDarshanList,
                deityCategories = viewModel.repository.deityCategories,
                initialDeityId = sub.deity?.id,
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.GitaChapterReader -> {
            val shlokasForChapter = viewModel.repository.keyShlokas.filter { it.chapterNumber == sub.chapter.number }
                .ifEmpty { listOf(viewModel.repository.keyShlokas.first()) }
            GitaReaderScreen(
                chapter = sub.chapter,
                shlokas = shlokasForChapter,
                allChapters = viewModel.repository.gitaChapters,
                onSelectChapter = { nextChapter ->
                    viewModel.openSubScreen(SubScreen.GitaChapterReader(nextChapter))
                },
                onBack = { viewModel.closeSubScreen() },
                onSaveBookmark = { ch, verse, text ->
                    val bmTitle = when (currentLanguage) {
                        "hi" -> "गीता अध्याय $ch, श्लोक $verse"
                        "en" -> "Gita Chapter $ch, Shloka $verse"
                        else -> "ગીતા અધ્યાય $ch, શ્લોક $verse"
                    }
                    viewModel.saveBookmark(
                        title = bmTitle,
                        snippet = text,
                        chapter = ch,
                        verse = verse
                    )
                }
            )
        }
        is SubScreen.JapMala -> {
            JapMalaScreen(
                lifetimeRounds = lifetimeRounds,
                lifetimeBeads = lifetimeBeads,
                onRecordMalaSession = { mantra, beads, rounds ->
                    viewModel.recordMalaSession(mantra, beads, rounds)
                },
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.TempleDirectory -> {
            TempleDirectoryScreen(
                temples = viewModel.repository.temples,
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.WallpaperStudio -> {
            WallpaperStudioScreen(
                wallpapers = viewModel.repository.wallpapers,
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.GlobalSearch -> {
            SearchScreen(
                allTracks = viewModel.repository.audioTracks,
                allTemples = viewModel.repository.temples,
                allChapters = viewModel.repository.gitaChapters,
                onSelectTrack = { track ->
                    onSelectAudioTrack(track)
                },
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.FavoritesAndBookmarks -> {
            FavoritesScreen(
                favorites = favorites,
                bookmarks = bookmarks,
                onPlayFavoriteAudio = { trackId ->
                    val track = viewModel.repository.audioTracks.find { it.id == trackId }
                    if (track != null) {
                        onSelectAudioTrack(track)
                    }
                },
                onDeleteFavorite = { viewModel.deleteFavorite(it) },
                onDeleteBookmark = { viewModel.deleteBookmark(it) },
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.Settings -> {
            SettingsScreen(
                currentLanguage = currentLanguage,
                isDarkTheme = isDarkTheme,
                selectedCity = selectedCity,
                morningReminder = morningReminder,
                eveningReminder = eveningReminder,
                onLanguageChange = { viewModel.setLanguage(it) },
                onThemeChange = { viewModel.setDarkTheme(it) },
                onCityChange = { viewModel.selectCity(it) },
                onMorningReminderChange = { viewModel.setMorningReminder(it) },
                onEveningReminderChange = { viewModel.setEveningReminder(it) },
                onResetMala = { viewModel.resetMalaHistory() },
                onClearFavorites = { viewModel.clearAllFavoritesAndBookmarks() },
                onNavigateToPrivacyPolicy = { viewModel.openSubScreen(SubScreen.PrivacyPolicy) },
                onNavigateToTerms = { viewModel.openSubScreen(SubScreen.TermsAndConditions) },
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.PrivacyPolicy -> {
            PrivacyPolicyScreen(
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.TermsAndConditions -> {
            TermsAndConditionsScreen(
                onBack = { viewModel.closeSubScreen() }
            )
        }
        is SubScreen.None -> {
            // Main Bottom Tabbed Interface
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    BhaktiTopBar(
                        title = AppStrings.appTitle(currentLanguage),
                        subtitle = AppStrings.appSubtitle(currentLanguage),
                        onSearchClick = { viewModel.openSubScreen(SubScreen.GlobalSearch) },
                        onFavoritesClick = { viewModel.openSubScreen(SubScreen.FavoritesAndBookmarks) },
                        onSettingsClick = { viewModel.openSubScreen(SubScreen.Settings) },
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { viewModel.setDarkTheme(!isDarkTheme) }
                    )
                },
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        // Persistent Mini Audio Player
                        MiniAudioPlayer(
                            playerState = audioState,
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            onOpenFullPlayer = { viewModel.openSubScreen(SubScreen.FullPlayer) },
                            onCloseMiniPlayer = { viewModel.dismissMiniPlayer() }
                        )

                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            BottomTab.entries.forEach { tab ->
                                val selected = currentTab == tab
                                val icon = when (tab) {
                                    BottomTab.HOME -> Icons.Default.Home
                                    BottomTab.LISTEN -> Icons.Default.MusicNote
                                    BottomTab.READ -> Icons.Default.Book
                                    BottomTab.PANCHANG -> Icons.Default.CalendarToday
                                    BottomTab.MORE -> Icons.Default.MoreHoriz
                                }

                                val tabLabel = when (tab) {
                                    BottomTab.HOME -> AppStrings.tabHome(currentLanguage)
                                    BottomTab.LISTEN -> AppStrings.tabListen(currentLanguage)
                                    BottomTab.READ -> AppStrings.tabRead(currentLanguage)
                                    BottomTab.PANCHANG -> AppStrings.tabPanchang(currentLanguage)
                                    BottomTab.MORE -> AppStrings.tabMore(currentLanguage)
                                }

                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(tab) },
                                    icon = {
                                        Icon(imageVector = icon, contentDescription = tabLabel)
                                    },
                                    label = {
                                        Text(
                                            text = tabLabel,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF1F1206),
                                        selectedTextColor = SaffronDark,
                                        indicatorColor = Color(0xFFFDE68A)
                                    ),
                                    modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TabTransition"
                    ) { tab ->
                        when (tab) {
                            BottomTab.HOME -> {
                                HomeScreen(
                                    panchangData = panchangData,
                                    dailyDarshan = currentHomeDarshan,
                                    gitaShloka = todayGitaShloka,
                                    deityCategories = viewModel.repository.deityCategories,
                                    featuredAarti = featuredAarti,
                                    featuredBhajan = featuredBhajan,
                                    upcomingFestivals = viewModel.repository.festivals,
                                    selectedDarshanDayOfWeek = selectedHomeDarshanDay,
                                    todayDayOfWeek = todaySystemDayOfWeek,
                                    currentLanguage = currentLanguage,
                                    onSelectDarshanDay = { viewModel.selectHomeDarshanDay(it) },
                                    onNextDarshanDay = { viewModel.nextHomeDarshanDay() },
                                    onPrevDarshanDay = { viewModel.prevHomeDarshanDay() },
                                    onResetDarshanDay = { viewModel.resetHomeDarshanToToday() },
                                    onSelectDeity = { viewModel.openSubScreen(SubScreen.DeityDarshanGallery(it)) },
                                    onSelectTrack = { track ->
                                        onSelectAudioTrack(track)
                                    },
                                    onPlayTrack = { track ->
                                        onPlayAudioDirect(track)
                                    },
                                    onNavigateToDarshan = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.DeityDarshanGallery())
                                        }
                                    },
                                    onNavigateToGita = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.selectTab(BottomTab.READ)
                                        }
                                    },
                                    onNavigateToPanchang = { viewModel.selectTab(BottomTab.PANCHANG) },
                                    onNavigateToJapMala = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.JapMala)
                                        }
                                    },
                                    onNavigateToTemples = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.TempleDirectory)
                                        }
                                    }
                                )
                            }
                            BottomTab.LISTEN -> {
                                AudioLibraryScreen(
                                    tracks = viewModel.repository.audioTracks,
                                    favoriteIds = favoriteIds,
                                    onSelectTrack = { track ->
                                        onSelectAudioTrack(track)
                                    },
                                    onPlayTrack = { track ->
                                        onPlayAudioDirect(track)
                                    },
                                    onToggleFavorite = { track ->
                                        viewModel.toggleFavorite(track, favoriteIds.contains(track.id))
                                    }
                                )
                            }
                            BottomTab.READ -> {
                                ReadScreen(
                                    scriptures = viewModel.repository.scriptures,
                                    gitaChapters = viewModel.repository.gitaChapters,
                                    onOpenGita = { chapter ->
                                        viewModel.openSubScreen(
                                            SubScreen.GitaChapterReader(chapter ?: viewModel.repository.gitaChapters.first())
                                        )
                                    },
                                    onSaveBookmark = { title, snippet, ch, verse ->
                                        viewModel.saveBookmark(title, snippet, ch, verse)
                                    }
                                )
                            }
                            BottomTab.PANCHANG -> {
                                PanchangScreen(
                                    panchangData = panchangData,
                                    festivals = festivalsForYear,
                                    selectedCity = selectedCity,
                                    currentLanguage = currentLanguage,
                                    onSelectCity = { viewModel.selectCity(it) },
                                    selectedYear = selectedCalendarYear,
                                    selectedMonth0 = selectedCalendarMonth0,
                                    calendarDays = calendarDays,
                                    onSelectDate = { y, m, d -> viewModel.selectCalendarDate(y, m, d) },
                                    onSelectYear = { y -> viewModel.setCalendarYear(y) },
                                    onSelectMonth0 = { m -> viewModel.setCalendarMonth(m) },
                                    onNextMonth = { viewModel.nextCalendarMonth() },
                                    onPrevMonth = { viewModel.previousCalendarMonth() },
                                    onResetToday = { viewModel.resetToToday() },
                                    onChangeDateByDays = { delta -> viewModel.changeDateByDays(delta) }
                                )
                            }
                            BottomTab.MORE -> {
                                MoreMenuScreen(
                                    onNavigateToJapMala = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.JapMala)
                                        }
                                    },
                                    onNavigateToTemples = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.TempleDirectory)
                                        }
                                    },
                                    onNavigateToWallpapers = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.WallpaperStudio)
                                        }
                                    },
                                    onNavigateToFavorites = {
                                        AdMobManager.showInterstitialAd(activity) {
                                            viewModel.openSubScreen(SubScreen.FavoritesAndBookmarks)
                                        }
                                    },
                                    onNavigateToSettings = { viewModel.openSubScreen(SubScreen.Settings) },
                                    onNavigateToPrivacyPolicy = { viewModel.openSubScreen(SubScreen.PrivacyPolicy) },
                                    onNavigateToTerms = { viewModel.openSubScreen(SubScreen.TermsAndConditions) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
