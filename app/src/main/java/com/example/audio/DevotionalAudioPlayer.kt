package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import com.example.R
import com.example.data.model.AudioTrack as DevotionalTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class VocalMode {
    HUMAN_VOCAL,
    TTS_RECITATION
}

data class AudioPlayerState(
    val currentTrack: DevotionalTrack? = null,
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val durationSeconds: Int = 0,
    val currentLineIndex: Int = 0,
    val lines: List<String> = emptyList(),
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val vocalMode: VocalMode = VocalMode.HUMAN_VOCAL,
    val isShuffle: Boolean = false,
    val speechRate: Float = 1.0f,
    val isVoiceEnabled: Boolean = true,
    val isBellAmbianceEnabled: Boolean = true,
    val isTtsActive: Boolean = false,
    val isBuffering: Boolean = false,
    val sleepTimerMinutesRemaining: Int? = null,
    val isMiniPlayerVisible: Boolean = false
)

/**
 * Authentic Devotional Audio Player for Bhakti Sagar.
 * Exclusively plays high-fidelity, real human devotional recordings with traditional temple
 * instruments (harmonium, dholak, manjira, and bells) with zero synthetic robotic TTS artifacts.
 */
class DevotionalAudioPlayer(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    private var vocalMediaPlayer: MediaPlayer? = null
    private var bellMediaPlayer: MediaPlayer? = null

    private var lineSyncJob: Job? = null
    private var tickerJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var playlist: List<DevotionalTrack> = emptyList()

    fun setPlaylist(tracks: List<DevotionalTrack>) {
        playlist = tracks
    }

    private fun isNetworkConnected(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val network = cm.activeNetwork ?: return false
            val cap = cm.getNetworkCapabilities(network) ?: return false
            cap.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }


    /**
     * Prepares and loads an audio track into player state WITHOUT starting playback.
     * Audio strictly does not start until the user explicitly presses the Play button.
     */
    fun prepareTrack(track: DevotionalTrack) {
        stopCurrentPlayback()

        val rawLines = track.lyricsGujarati
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("//") }
        val lines = if (rawLines.isEmpty()) listOf(track.titleGujarati) else rawLines

        _playerState.update {
            it.copy(
                currentTrack = track,
                isPlaying = false,
                currentPositionSeconds = 0,
                durationSeconds = track.durationSeconds.coerceAtLeast(30),
                lines = lines,
                currentLineIndex = 0,
                isMiniPlayerVisible = true,
                isBuffering = false,
                vocalMode = VocalMode.HUMAN_VOCAL
            )
        }
    }

    fun playTrack(track: DevotionalTrack) {
        if (!isNetworkConnected()) {
            stopAndReset()
            return
        }
        stopCurrentPlayback()

        val rawLines = track.lyricsGujarati
            .lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("//") }
        val lines = if (rawLines.isEmpty()) listOf(track.titleGujarati) else rawLines

        _playerState.update {
            it.copy(
                currentTrack = track,
                isPlaying = true,
                currentPositionSeconds = 0,
                durationSeconds = track.durationSeconds.coerceAtLeast(30),
                lines = lines,
                currentLineIndex = 0,
                isMiniPlayerVisible = true,
                isBuffering = false,
                vocalMode = VocalMode.HUMAN_VOCAL
            )
        }

        startHumanVocalPlayback(track)
        startProgressTicker()
    }

    /**
     * Plays genuine human vocal recording through MediaPlayer from bundled raw audio.
     */
    private fun startHumanVocalPlayback(track: DevotionalTrack) {
        try {
            vocalMediaPlayer?.stop()
            vocalMediaPlayer?.release()
        } catch (_: Exception) {}
        vocalMediaPlayer = null

        val rawResId = track.rawResId ?: R.raw.aarti_ganesh_deva

        // Priority 1: Direct MediaPlayer.create with applicationContext
        try {
            val player = MediaPlayer.create(context.applicationContext, rawResId)
            if (player != null) {
                configureAndStartPlayer(player, track)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Priority 2: Try openRawResourceFd
        try {
            val afd = context.resources.openRawResourceFd(rawResId)
            if (afd != null) {
                val player = MediaPlayer()
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                player.prepare()
                afd.close()
                configureAndStartPlayer(player, track)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Priority 3: Fallback to bundled sacred bell recording
        try {
            val fallback = MediaPlayer.create(context.applicationContext, R.raw.temple_bell_aarti)
            if (fallback != null) {
                configureAndStartPlayer(fallback, track)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configureAndStartPlayer(player: MediaPlayer, track: DevotionalTrack) {
        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        // Apply speech/playback rate if not default
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val speed = _playerState.value.speechRate
                if (speed != 1.0f) {
                    player.playbackParams = player.playbackParams.setSpeed(speed)
                }
            } catch (_: Exception) {}
        }

        // Apply volume
        val volume = if (_playerState.value.isVoiceEnabled) 1.0f else 0.1f
        try {
            player.setVolume(volume, volume)
        } catch (_: Exception) {}

        val actualDuration = (player.duration / 1000).coerceAtLeast(track.durationSeconds)
        _playerState.update {
            it.copy(
                durationSeconds = actualDuration,
                isPlaying = true,
                isBuffering = false
            )
        }

        player.start()
        vocalMediaPlayer = player
        setupPlayerCallbacks(player)
        startLyricLineSync(actualDuration)
    }

    private fun setupPlayerCallbacks(player: MediaPlayer) {
        player.setOnCompletionListener {
            handleTrackCompletion()
        }
        player.setOnErrorListener { _, _, _ ->
            _playerState.update { it.copy(isBuffering = false) }
            false
        }
    }

    /**
     * Synchronizes lyric line highlighting to match human vocal audio playback duration.
     */
    private fun startLyricLineSync(totalDurationSeconds: Int) {
        lineSyncJob?.cancel()
        val lines = _playerState.value.lines
        if (lines.isEmpty()) return

        lineSyncJob = scope.launch(Dispatchers.Main) {
            while (isActive && _playerState.value.isPlaying) {
                val currentSec = vocalMediaPlayer?.let {
                    try { if (it.isPlaying) it.currentPosition / 1000 else null } catch (_: Exception) { null }
                } ?: _playerState.value.currentPositionSeconds

                val lineCount = lines.size
                val calculatedLine = ((currentSec.toFloat() / totalDurationSeconds.coerceAtLeast(1)) * lineCount)
                    .toInt()
                    .coerceIn(0, lineCount - 1)

                if (calculatedLine != _playerState.value.currentLineIndex) {
                    _playerState.update { it.copy(currentLineIndex = calculatedLine) }
                }
                delay(800L)
            }
        }
    }

    private fun handleTrackCompletion() {
        when (_playerState.value.repeatMode) {
            RepeatMode.ONE -> {
                seekTo(0)
                resume()
            }
            RepeatMode.ALL -> {
                playNext()
            }
            RepeatMode.OFF -> {
                pause()
                _playerState.update { it.copy(currentPositionSeconds = it.durationSeconds) }
            }
        }
    }

    fun jumpToLine(index: Int) {
        val lines = _playerState.value.lines
        if (lines.isEmpty()) return
        val bounded = index.coerceIn(0, lines.size - 1)
        val totalSec = _playerState.value.durationSeconds.coerceAtLeast(1)
        val targetSec = (bounded.toFloat() / lines.size * totalSec).toInt()
        seekTo(targetSec)
    }

    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        _playerState.update { it.copy(isPlaying = false) }
        try {
            vocalMediaPlayer?.pause()
        } catch (_: Exception) {}
        try {
            bellMediaPlayer?.pause()
        } catch (_: Exception) {}
        lineSyncJob?.cancel()
        tickerJob?.cancel()
    }

    fun resume() {
        if (!isNetworkConnected()) {
            stopAndReset()
            return
        }
        val track = _playerState.value.currentTrack ?: return
        _playerState.update { it.copy(isPlaying = true) }

        try {
            if (vocalMediaPlayer != null) {
                vocalMediaPlayer?.start()
            } else {
                startHumanVocalPlayback(track)
            }
        } catch (_: Exception) {
            startHumanVocalPlayback(track)
        }

        try {
            if (_playerState.value.isBellAmbianceEnabled && bellMediaPlayer != null) {
                bellMediaPlayer?.start()
            }
        } catch (_: Exception) {}

        startLyricLineSync(_playerState.value.durationSeconds)
        startProgressTicker()
    }

    fun seekTo(seconds: Int) {
        val duration = _playerState.value.durationSeconds.coerceAtLeast(1)
        val bounded = seconds.coerceIn(0, duration)
        _playerState.update { it.copy(currentPositionSeconds = bounded) }

        try {
            vocalMediaPlayer?.seekTo(bounded * 1000)
        } catch (_: Exception) {}

        val lines = _playerState.value.lines
        if (lines.isNotEmpty()) {
            val targetLine = (bounded.toFloat() / duration * lines.size).toInt().coerceIn(0, lines.size - 1)
            _playerState.update { it.copy(currentLineIndex = targetLine) }
        }
    }

    fun playNext() {
        val current = _playerState.value.currentTrack ?: return
        if (playlist.isEmpty()) return

        val currentIndex = playlist.indexOfFirst { it.id == current.id }
        val nextIndex = if (_playerState.value.isShuffle) {
            playlist.indices.random()
        } else {
            if (currentIndex in 0 until playlist.size - 1) currentIndex + 1 else 0
        }
        val nextTrack = playlist[nextIndex]
        if (_playerState.value.isPlaying) {
            playTrack(nextTrack)
        } else {
            prepareTrack(nextTrack)
        }
    }

    fun playPrevious() {
        val current = _playerState.value.currentTrack ?: return
        if (playlist.isEmpty()) return

        val currentIndex = playlist.indexOfFirst { it.id == current.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else playlist.size - 1
        val prevTrack = playlist[prevIndex]
        if (_playerState.value.isPlaying) {
            playTrack(prevTrack)
        } else {
            prepareTrack(prevTrack)
        }
    }

    fun toggleRepeat() {
        val next = when (_playerState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _playerState.update { it.copy(repeatMode = next) }
    }

    fun toggleShuffle() {
        _playerState.update { it.copy(isShuffle = !it.isShuffle) }
    }

    fun toggleVocalMode() {
        // Human vocal audio is the definitive and only mode
        _playerState.update { it.copy(vocalMode = VocalMode.HUMAN_VOCAL) }
    }

    fun setSpeechRate(rate: Float) {
        val clamped = rate.coerceIn(0.6f, 1.4f)
        _playerState.update { it.copy(speechRate = clamped) }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                vocalMediaPlayer?.let { player ->
                    player.playbackParams = player.playbackParams.setSpeed(clamped)
                }
            }
        } catch (_: Exception) {}
    }

    fun toggleVoiceEnabled() {
        val newVoice = !_playerState.value.isVoiceEnabled
        _playerState.update { it.copy(isVoiceEnabled = newVoice) }
        val vol = if (newVoice) 1.0f else 0.1f
        try {
            vocalMediaPlayer?.setVolume(vol, vol)
        } catch (_: Exception) {}
    }

    fun toggleBellAmbiance() {
        val newBell = !_playerState.value.isBellAmbianceEnabled
        _playerState.update { it.copy(isBellAmbianceEnabled = newBell) }
        if (!newBell) {
            try { bellMediaPlayer?.pause() } catch (_: Exception) {}
        } else {
            try {
                if (_playerState.value.isPlaying) {
                    bellMediaPlayer?.start()
                }
            } catch (_: Exception) {}
        }
    }

    fun setSleepTimer(minutes: Int?) {
        sleepTimerJob?.cancel()
        if (minutes == null || minutes <= 0) {
            _playerState.update { it.copy(sleepTimerMinutesRemaining = null) }
            return
        }

        _playerState.update { it.copy(sleepTimerMinutesRemaining = minutes) }
        sleepTimerJob = scope.launch {
            var rem = minutes
            while (rem > 0 && isActive) {
                delay(60000L)
                rem -= 1
                _playerState.update { it.copy(sleepTimerMinutesRemaining = if (rem > 0) rem else null) }
            }
            pause()
        }
    }

    fun dismissMiniPlayer() {
        pause()
        _playerState.update { it.copy(isMiniPlayerVisible = false) }
    }

    private fun startProgressTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch(Dispatchers.Main) {
            while (isActive && _playerState.value.isPlaying) {
                delay(500L)
                if (!isNetworkConnected()) {
                    stopAndReset()
                    break
                }
                val mediaSec = vocalMediaPlayer?.let {
                    try {
                        if (it.isPlaying) (it.currentPosition / 1000) else null
                    } catch (_: Exception) { null }
                }

                val currentSec = mediaSec ?: (_playerState.value.currentPositionSeconds + 1)
                val totalSec = _playerState.value.durationSeconds.coerceAtLeast(1)
                _playerState.update { it.copy(currentPositionSeconds = currentSec.coerceIn(0, totalSec)) }
            }
        }
    }

    private fun stopCurrentPlayback() {
        lineSyncJob?.cancel()
        lineSyncJob = null
        tickerJob?.cancel()
        tickerJob = null

        try {
            vocalMediaPlayer?.stop()
            vocalMediaPlayer?.release()
        } catch (_: Exception) {}
        vocalMediaPlayer = null

        try {
            bellMediaPlayer?.stop()
            bellMediaPlayer?.release()
        } catch (_: Exception) {}
        bellMediaPlayer = null
    }

    fun stopAndReset() {
        _playerState.update {
            it.copy(
                isPlaying = false,
                isMiniPlayerVisible = false,
                currentPositionSeconds = 0
            )
        }
        stopCurrentPlayback()
    }

    fun release() {
        stopCurrentPlayback()
        sleepTimerJob?.cancel()
    }
}
