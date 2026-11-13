package com.example.services

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.AdMobManager

/**
 * AdMobService: Helper service managing rewarded bonus track unlocks and sacred session adherence.
 * Note: 30-minute Ad-Free mode has been completely removed as per business requirements.
 */
object AdMobService {

    private const val PREFS_NAME = "bhakti_unlocked_bonus_prefs"
    private const val KEY_UNLOCKED_TRACKS = "unlocked_bonus_tracks"
    private const val KEY_UNLOCKED_WALLPAPERS = "unlocked_hd_wallpapers"

    /**
     * Checks if a bonus audio track has been unlocked by watching a rewarded ad.
     */
    fun isTrackUnlocked(context: Context, trackId: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val unlockedSet = prefs.getStringSet(KEY_UNLOCKED_TRACKS, emptySet()) ?: emptySet()
        return unlockedSet.contains(trackId)
    }

    /**
     * Permanently unlocks a bonus audio track after official earned-reward callback.
     */
    fun unlockTrack(context: Context, trackId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentSet = prefs.getStringSet(KEY_UNLOCKED_TRACKS, emptySet())?.toMutableSet() ?: mutableSetOf()
        currentSet.add(trackId)
        prefs.edit().putStringSet(KEY_UNLOCKED_TRACKS, currentSet).apply()
    }

    /**
     * Retrieves the set of all unlocked bonus track IDs.
     */
    fun getUnlockedTracks(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_UNLOCKED_TRACKS, emptySet()) ?: emptySet()
    }

    /**
     * Checks if a wallpaper has been unlocked in HD via rewarded ad.
     */
    fun isWallpaperUnlocked(context: Context, wallpaperId: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val unlockedSet = prefs.getStringSet(KEY_UNLOCKED_WALLPAPERS, emptySet()) ?: emptySet()
        return unlockedSet.contains(wallpaperId)
    }

    /**
     * Permanently unlocks a wallpaper in HD after official earned-reward callback.
     */
    fun unlockWallpaper(context: Context, wallpaperId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentSet = prefs.getStringSet(KEY_UNLOCKED_WALLPAPERS, emptySet())?.toMutableSet() ?: mutableSetOf()
        currentSet.add(wallpaperId)
        prefs.edit().putStringSet(KEY_UNLOCKED_WALLPAPERS, currentSet).apply()
    }

    /**
     * Shows Interstitial Ad if no active devotional chanting or audio session is active.
     */
    fun showInterstitialIfAllowed(context: Context, onComplete: () -> Unit) {
        if (AdMobManager.isSacredSessionActive()) {
            onComplete()
            return
        }
        onComplete()
    }
}

/**
 * DevotionalAdBanner: Visually respectful M3 banner placeholder that adheres to Google AdMob standards
 * and clearly separates promotional space from sacred devotional content.
 */
@Composable
fun DevotionalAdBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF8E7))
            .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFB300))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "જાહેરાત",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E1E00)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ભક્તિ સાગર - તમારા મોબાઇલમાં એક ડિજિટલ મંદિર",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D5643),
                maxLines = 1
            )
        }
    }
}
