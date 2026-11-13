package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlayerState
import com.example.audio.DevotionalAudioPlayer
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.FavoriteEntity
import com.example.data.model.AudioTrack
import com.example.data.model.DailyDarshanItem
import com.example.data.model.DeityCategory
import com.example.data.model.DevotionalScripture
import com.example.data.model.GitaChapter
import com.example.data.model.GitaShloka
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangData
import com.example.data.model.TempleInfo
import com.example.data.model.WallpaperItem
import com.example.data.repository.BhaktiRepository
import com.example.data.repository.PanchangCalculator
import com.example.data.repository.PreferencesRepository
import com.example.util.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

enum class BottomTab(val titleGujarati: String) {
    HOME("હોમ"),
    LISTEN("સાંભળો"),
    READ("વાંચો"),
    PANCHANG("પંચાંગ"),
    MORE("વધુ")
}

sealed class SubScreen {
    data object None : SubScreen()
    data object FullPlayer : SubScreen()
    data class DeityDarshanGallery(val deity: DeityCategory? = null) : SubScreen()
    data class GitaChapterReader(val chapter: GitaChapter) : SubScreen()
    data object JapMala : SubScreen()
    data object TempleDirectory : SubScreen()
    data object WallpaperStudio : SubScreen()
    data object GlobalSearch : SubScreen()
    data object FavoritesAndBookmarks : SubScreen()
    data object Settings : SubScreen()
    data object PrivacyPolicy : SubScreen()
    data object TermsAndConditions : SubScreen()
}

class BhaktiViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = BhaktiRepository(db.devotionalDao())
    val preferences = PreferencesRepository(application)
    val audioPlayer = DevotionalAudioPlayer(application)
    val networkObserver = NetworkObserver(application)

    private val _isOnline = MutableStateFlow(networkObserver.isCurrentlyConnected())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    fun onInternetLost() {
        _isOnline.value = false
        audioPlayer.stopAndReset()
        _activeSubScreen.value = SubScreen.None
    }

    fun retryConnection(): Boolean {
        val connected = networkObserver.isCurrentlyConnected()
        _isOnline.value = connected
        if (!connected) {
            onInternetLost()
        }
        return connected
    }

    // Current Navigation State
    private val _currentTab = MutableStateFlow(BottomTab.HOME)
    val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

    private val _activeSubScreen = MutableStateFlow<SubScreen>(SubScreen.None)
    val activeSubScreen: StateFlow<SubScreen> = _activeSubScreen.asStateFlow()

    // Preferences Flows
    val currentLanguage: StateFlow<String> = preferences.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "gu")

    val isDarkTheme: StateFlow<Boolean> = preferences.darkThemeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val selectedCity: StateFlow<String> = preferences.selectedCityFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "અમદાવાદ")

    val morningReminder: StateFlow<Boolean> = preferences.morningReminderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val eveningReminder: StateFlow<Boolean> = preferences.eveningReminderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Selected Date & Interactive Calendar State (2026-2030)
    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate: StateFlow<Calendar> = _selectedDate.asStateFlow()

    private val _selectedCalendarYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR).coerceIn(2026, 2030))
    val selectedCalendarYear: StateFlow<Int> = _selectedCalendarYear.asStateFlow()

    private val _selectedCalendarMonth0 = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH))
    val selectedCalendarMonth0: StateFlow<Int> = _selectedCalendarMonth0.asStateFlow()

    val calendarDays: StateFlow<List<com.example.data.model.GujaratiCalendarDay>> = combine(
        _selectedCalendarYear,
        _selectedCalendarMonth0
    ) { year, month0 ->
        PanchangCalculator.getCalendarMonth(year, month0, Calendar.getInstance())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PanchangCalculator.getCalendarMonth(2026, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance()))

    val festivalsForSelectedYear: StateFlow<List<HinduFestival>> = _selectedCalendarYear.combine(MutableStateFlow(Unit)) { year, _ ->
        PanchangCalculator.getFestivalsForYear(year)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PanchangCalculator.getFestivalsForYear(2026))

    // Panchang for selected date & city
    private val _panchangState = MutableStateFlow(PanchangCalculator.calculatePanchang(Calendar.getInstance(), "અમદાવાદ"))
    val panchangState: StateFlow<PanchangData> = _panchangState.asStateFlow()

    // Day-wise Darshan for Home Screen (Automatically changes by current day of week, and user can tap any day)
    val todaySystemDayOfWeek: Int get() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    private val _selectedHomeDarshanDay = MutableStateFlow(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    val selectedHomeDarshanDay: StateFlow<Int> = _selectedHomeDarshanDay.asStateFlow()

    val currentHomeDarshan: StateFlow<DailyDarshanItem> = _selectedHomeDarshanDay
        .combine(MutableStateFlow(Unit)) { dayOfWeek, _ ->
            repository.getDarshanForDayOfWeek(dayOfWeek)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getTodayDarshan())

    val todayGitaShloka: StateFlow<GitaShloka> = _selectedDate
        .combine(MutableStateFlow(Unit)) { date, _ ->
            repository.getTodayGitaShloka(date)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getTodayGitaShloka())

    val featuredAartiForDay: StateFlow<AudioTrack> = _selectedHomeDarshanDay
        .combine(MutableStateFlow(Unit)) { dayOfWeek, _ ->
            repository.getFeaturedAartiForDay(dayOfWeek)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getFeaturedAartiForDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))

    val featuredBhajanForDay: StateFlow<AudioTrack> = _selectedHomeDarshanDay
        .combine(MutableStateFlow(Unit)) { dayOfWeek, _ ->
            repository.getFeaturedBhajanForDay(dayOfWeek)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getFeaturedBhajanForDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))

    fun selectHomeDarshanDay(dayOfWeek: Int) {
        _selectedHomeDarshanDay.value = dayOfWeek.coerceIn(Calendar.SUNDAY, Calendar.SATURDAY)
    }

    fun nextHomeDarshanDay() {
        val cur = _selectedHomeDarshanDay.value
        val next = if (cur >= Calendar.SATURDAY) Calendar.SUNDAY else cur + 1
        _selectedHomeDarshanDay.value = next
    }

    fun prevHomeDarshanDay() {
        val cur = _selectedHomeDarshanDay.value
        val prev = if (cur <= Calendar.SUNDAY) Calendar.SATURDAY else cur - 1
        _selectedHomeDarshanDay.value = prev
    }

    fun resetHomeDarshanToToday() {
        _selectedHomeDarshanDay.value = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    // Audio Player State
    val audioPlayerState: StateFlow<AudioPlayerState> = audioPlayer.playerState

    // Favorites & Bookmarks
    val favorites: StateFlow<List<FavoriteEntity>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lifetimeMalaRounds: StateFlow<Int> = repository.totalMalaRounds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
        .combine(MutableStateFlow(0)) { rounds, _ -> rounds ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val lifetimeMalaBeads: StateFlow<Int> = repository.totalMalaBeads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
        .combine(MutableStateFlow(0)) { beads, _ -> beads ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        audioPlayer.setPlaylist(repository.audioTracks)

        viewModelScope.launch {
            selectedCity.collect { city ->
                _panchangState.value = PanchangCalculator.calculatePanchang(_selectedDate.value, city)
            }
        }

        viewModelScope.launch {
            networkObserver.isConnectedFlow.collect { connected ->
                _isOnline.value = connected
                if (!connected) {
                    onInternetLost()
                }
            }
        }
    }

    fun selectCalendarDate(year: Int, month1: Int, day: Int) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month1 - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }
        _selectedDate.value = cal
        _selectedCalendarYear.value = year.coerceIn(2026, 2030)
        _selectedCalendarMonth0.value = month1 - 1
        _panchangState.value = PanchangCalculator.calculatePanchang(cal, selectedCity.value)
    }

    fun resetToToday() {
        val now = Calendar.getInstance()
        _selectedDate.value = now
        _selectedCalendarYear.value = now.get(Calendar.YEAR).coerceIn(2026, 2030)
        _selectedCalendarMonth0.value = now.get(Calendar.MONTH)
        _panchangState.value = PanchangCalculator.calculatePanchang(now, selectedCity.value)
    }

    fun changeDateByDays(days: Int) {
        val newCal = (_selectedDate.value.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, days)
        }
        val y = newCal.get(Calendar.YEAR)
        if (y in 2026..2030) {
            _selectedDate.value = newCal
            _selectedCalendarYear.value = y
            _selectedCalendarMonth0.value = newCal.get(Calendar.MONTH)
            _panchangState.value = PanchangCalculator.calculatePanchang(newCal, selectedCity.value)
        }
    }

    fun setCalendarYear(year: Int) {
        _selectedCalendarYear.value = year.coerceIn(2026, 2030)
    }

    fun setCalendarMonth(month0: Int) {
        _selectedCalendarMonth0.value = month0.coerceIn(0, 11)
    }

    fun nextCalendarMonth() {
        val curM = _selectedCalendarMonth0.value
        val curY = _selectedCalendarYear.value
        if (curM < 11) {
            _selectedCalendarMonth0.value = curM + 1
        } else if (curY < 2030) {
            _selectedCalendarYear.value = curY + 1
            _selectedCalendarMonth0.value = 0
        }
    }

    fun previousCalendarMonth() {
        val curM = _selectedCalendarMonth0.value
        val curY = _selectedCalendarYear.value
        if (curM > 0) {
            _selectedCalendarMonth0.value = curM - 1
        } else if (curY > 2026) {
            _selectedCalendarYear.value = curY - 1
            _selectedCalendarMonth0.value = 11
        }
    }


    fun selectTab(tab: BottomTab) {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        _currentTab.value = tab
        _activeSubScreen.value = SubScreen.None
    }

    fun openSubScreen(screen: SubScreen) {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        _activeSubScreen.value = screen
    }

    fun closeSubScreen() {
        _activeSubScreen.value = SubScreen.None
    }

    fun selectCity(city: String) {
        viewModelScope.launch {
            preferences.setSelectedCity(city)
            _panchangState.value = PanchangCalculator.calculatePanchang(_selectedDate.value, city)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch { preferences.setLanguage(lang) }
    }

    fun setDarkTheme(dark: Boolean) {
        viewModelScope.launch { preferences.setDarkTheme(dark) }
    }

    fun setMorningReminder(enabled: Boolean) {
        viewModelScope.launch { preferences.setMorningReminder(enabled) }
    }

    fun setEveningReminder(enabled: Boolean) {
        viewModelScope.launch { preferences.setEveningReminder(enabled) }
    }

    // Audio Playback
    fun selectTrack(track: AudioTrack) {
        audioPlayer.prepareTrack(track)
        viewModelScope.launch { repository.addRecentPlay(track) }
    }

    fun playTrack(track: AudioTrack) {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        audioPlayer.playTrack(track)
        viewModelScope.launch { repository.addRecentPlay(track) }
    }

    fun togglePlayPause() {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        audioPlayer.togglePlayPause()
    }

    fun seekTo(seconds: Int) {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        audioPlayer.seekTo(seconds)
    }

    fun playNext() {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        audioPlayer.playNext()
    }

    fun playPrevious() {
        if (!networkObserver.isCurrentlyConnected()) {
            onInternetLost()
            return
        }
        audioPlayer.playPrevious()
    }
    fun toggleRepeat() = audioPlayer.toggleRepeat()
    fun toggleShuffle() = audioPlayer.toggleShuffle()
    fun setSleepTimer(minutes: Int?) = audioPlayer.setSleepTimer(minutes)
    fun dismissMiniPlayer() = audioPlayer.dismissMiniPlayer()
    fun jumpToLine(index: Int) = audioPlayer.jumpToLine(index)
    fun setSpeechRate(rate: Float) = audioPlayer.setSpeechRate(rate)
    fun toggleVoiceEnabled() = audioPlayer.toggleVoiceEnabled()
    fun toggleBellAmbiance() = audioPlayer.toggleBellAmbiance()
    fun toggleVocalMode() = audioPlayer.toggleVocalMode()

    // Favorites & Bookmarks
    fun toggleFavorite(track: AudioTrack, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(track.id, track.titleGujarati, track.category.name, track.deityNameGujarati, isFav)
        }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch { repository.toggleFavorite(id, "", "", "", true) }
    }

    fun saveBookmark(title: String, snippet: String, chapter: Int, verse: Int) {
        viewModelScope.launch {
            val id = "bm_${chapter}_${verse}_${System.currentTimeMillis()}"
            repository.saveBookmark(
                BookmarkEntity(
                    id = id,
                    scriptureId = "gita",
                    chapterIndex = chapter,
                    verseIndex = verse,
                    titleGujarati = title,
                    snippetGujarati = snippet
                )
            )
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch { repository.removeBookmark(id) }
    }

    fun recordMalaSession(mantra: String, beads: Int, rounds: Int) {
        viewModelScope.launch {
            repository.recordJapMalaSession(mantra, beads, rounds)
        }
    }

    fun clearAllFavoritesAndBookmarks() {
        viewModelScope.launch {
            repository.clearAllFavorites()
            repository.clearAllBookmarks()
        }
    }

    fun resetMalaHistory() {
        viewModelScope.launch {
            repository.resetMalaHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
