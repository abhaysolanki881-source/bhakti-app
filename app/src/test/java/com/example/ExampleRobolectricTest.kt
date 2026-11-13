package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import android.media.MediaPlayer
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Bhakti Sagar", appName)
  }

  @Test
  fun `all raw audio resources exist and have valid data`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val audioResources = listOf(
        "aarti_ganesh_deva" to R.raw.aarti_ganesh_deva,
        "aarti_om_jai_jagdish" to R.raw.aarti_om_jai_jagdish,
        "aarti_jay_adhya_shakti" to R.raw.aarti_jay_adhya_shakti,
        "aarti_om_jai_shiv_omkara" to R.raw.aarti_om_jai_shiv_omkara,
        "bhajan_vaishnav_jan_to" to R.raw.bhajan_vaishnav_jan_to,
        "gayatri_vedic_chant" to R.raw.gayatri_vedic_chant,
        "mantra_mahamrityunjaya" to R.raw.mantra_mahamrityunjaya,
        "kirtan_hare_krishna_mahamantra" to R.raw.kirtan_hare_krishna_mahamantra,
        "dhun_swaminarayan" to R.raw.dhun_swaminarayan,
        "aarti_kije_hanuman_lala" to R.raw.aarti_kije_hanuman_lala,
        "aarti_kunj_bihari_ki" to R.raw.aarti_kunj_bihari_ki,
        "chalisa_shree_hanuman" to R.raw.chalisa_shree_hanuman,
        "stuti_shri_ramchandra_kripalu" to R.raw.stuti_shri_ramchandra_kripalu,
        "mala_shree_ram" to R.raw.mala_shree_ram,
        "mala_hare_krishna" to R.raw.mala_hare_krishna,
        "mala_om_namah_shivaya" to R.raw.mala_om_namah_shivaya,
        "mala_hanuman" to R.raw.mala_hanuman,
        "mala_ganesh" to R.raw.mala_ganesh,
        "mala_ambe_mata" to R.raw.mala_ambe_mata,
        "mala_swaminarayan" to R.raw.mala_swaminarayan,
        "mala_gayatri" to R.raw.mala_gayatri,
        "mala_mahamrityunjaya" to R.raw.mala_mahamrityunjaya
    )

    for ((name, resId) in audioResources) {
        val inputStream = context.resources.openRawResource(resId)
        assertNotNull("Resource $name should open as input stream", inputStream)
        val available = inputStream.available()
        assertTrue("Resource $name should have > 1000 bytes, got $available", available > 1000)
        inputStream.close()
    }
  }

  @Test
  fun `test AdMobConfig rewarded ad unit IDs are correct`() {
    // Production Rewarded ID
    assertEquals("ca-app-pub-4890481636897653/2329086681", com.example.ads.AdMobConfig.PROD_REWARDED_AD_UNIT_ID)
    // Google Official Test Rewarded ID
    assertEquals("ca-app-pub-3940256099942544/5224354917", com.example.ads.AdMobConfig.TEST_REWARDED_AD_UNIT_ID)
    // Production App ID
    assertEquals("ca-app-pub-4890481636897653~2027648440", com.example.ads.AdMobConfig.PROD_APP_ID)

    val activeRewardedId = com.example.ads.AdMobConfig.getRewardedAdUnitId()
    assertNotNull(activeRewardedId)
    assertTrue(activeRewardedId.isNotEmpty())
  }

  @Test
  fun `test AdMobService bonus track unlock persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val testTrackId = "test_bonus_track_${System.currentTimeMillis()}"

    // Initially should be locked
    org.junit.Assert.assertFalse("Track should initially be locked", com.example.services.AdMobService.isTrackUnlocked(context, testTrackId))

    // Unlock after earned reward
    com.example.services.AdMobService.unlockTrack(context, testTrackId)

    // Should now be unlocked
    assertTrue("Track must be unlocked after reward", com.example.services.AdMobService.isTrackUnlocked(context, testTrackId))
    assertTrue("Unlocked tracks set must contain track", com.example.services.AdMobService.getUnlockedTracks(context).contains(testTrackId))
  }

  @Test
  fun `test normal audio tracks are free and bonus tracks are designated`() {
    val repo = com.example.data.repository.BhaktiRepository()
    val allTracks = repo.audioTracks

    // Normal tracks must NOT be bonus (100% free access)
    val freeTracks = allTracks.filter { !it.isBonus }
    assertTrue("Majority of tracks must be 100% free, found ${freeTracks.size}", freeTracks.size >= 10)

    val ganeshAarti = allTracks.first { it.id == "aarti_ganesh" }
    org.junit.Assert.assertFalse("Ganesh Aarti must be free", ganeshAarti.isBonus)

    val jagdishAarti = allTracks.first { it.id == "aarti_jagdish" }
    org.junit.Assert.assertFalse("Om Jai Jagdish must be free", jagdishAarti.isBonus)

    // Explicit designated bonus tracks
    val bonusTracks = allTracks.filter { it.isBonus }
    assertTrue("There should be designated bonus tracks", bonusTracks.isNotEmpty())
    assertTrue("Maha Mrityunjaya should be a designated bonus track", bonusTracks.any { it.id == "mantra_mahamrityunjaya" })
  }

  @Test
  fun `test bonus strings localized in all languages`() {
    for (lang in listOf("gu", "hi", "en")) {
      val badge = com.example.ui.strings.AppStrings.bonusBadge(lang)
      assertTrue("Bonus badge must not be empty for $lang", badge.isNotEmpty())

      val unlocked = com.example.ui.strings.AppStrings.unlockedBadge(lang)
      assertTrue("Unlocked badge must not be empty for $lang", unlocked.isNotEmpty())

      val title = com.example.ui.strings.AppStrings.bonusAudioTitle(lang)
      assertTrue("Title must not be empty for $lang", title.isNotEmpty())

      val msg = com.example.ui.strings.AppStrings.bonusAudioMessage("Test", lang)
      assertTrue("Message must contain track title for $lang", msg.contains("Test"))

      val freeNote = com.example.ui.strings.AppStrings.bonusAudioFreeNote(lang)
      assertTrue("Free note must not be empty for $lang", freeNote.isNotEmpty())
    }
  }

  @Test
  fun `test AdMobService HD wallpaper unlock persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val testWallId = "test_wallpaper_${System.currentTimeMillis()}"

    org.junit.Assert.assertFalse("Wallpaper should initially be locked", com.example.services.AdMobService.isWallpaperUnlocked(context, testWallId))
    com.example.services.AdMobService.unlockWallpaper(context, testWallId)
    assertTrue("Wallpaper must remain unlocked after earned reward", com.example.services.AdMobService.isWallpaperUnlocked(context, testWallId))
  }

  @Test
  fun `test all five AdMob formats and Play Console app_name compliance`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertTrue("App name must be <= 30 chars for Play Console", appName.length <= 30)
    org.junit.Assert.assertFalse("App name must not contain promotional buzzwords", appName.contains("Free", ignoreCase = true) || appName.contains("Best", ignoreCase = true))

    assertEquals("ca-app-pub-4890481636897653/7309950620", com.example.ads.AdMobConfig.PROD_APP_OPEN_AD_UNIT_ID)
    assertEquals("ca-app-pub-4890481636897653/4150589766", com.example.ads.AdMobConfig.PROD_BANNER_AD_UNIT_ID)
    assertEquals("ca-app-pub-4890481636897653/6553391823", com.example.ads.AdMobConfig.PROD_INTERSTITIAL_AD_UNIT_ID)
    assertEquals("ca-app-pub-4890481636897653/6843046766", com.example.ads.AdMobConfig.PROD_NATIVE_AD_UNIT_ID)
    assertEquals("ca-app-pub-4890481636897653/2329086681", com.example.ads.AdMobConfig.PROD_REWARDED_AD_UNIT_ID)

    assertTrue("Debug tests must run in test mode", com.example.ads.AdMobConfig.isTestMode)
    assertEquals(com.example.ads.AdMobConfig.TEST_REWARDED_AD_UNIT_ID, com.example.ads.AdMobConfig.getRewardedAdUnitId())
  }
}
