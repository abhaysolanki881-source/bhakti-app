package com.example.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.services.AdMobService
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.common.RequestConfiguration
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoader
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdRequest
import com.google.android.libraries.ads.mobile.sdk.rewarded.OnUserEarnedRewardListener
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback
import com.example.ui.strings.AppStrings
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

/**
 * Production-ready Next-Gen Google Mobile Ads Manager for Bhakti Sagar.
 *
 * Implements all five formats via `com.google.android.libraries.ads.mobile.sdk.*`:
 * 1. App Open Ads (Suppressed on first-launch & cold start, shown on warm start after >= 3s background)
 * 2. Banner Ads (Adaptive M3 container, maximized across all suitable screens)
 * 3. Interstitial Ads (Natural transitions with 35s cooldown, skipped during prayer/audio sessions)
 * 4. Native Ads (Unified temple, bhajan, scripture, and festival list cards)
 * 5. Rewarded Ads (100% Optional, unlocks selected Bonus/Premium audio tracks permanently)
 *
 * Note: 30-minute Ad-Free mode is completely removed. Normal audio and Jap Mala remain 100% free.
 */
object AdMobManager {

    private const val TAG = "AdMobManager"
    private const val PREFS_NAME = "bhakti_admob_prefs"
    private const val KEY_FIRST_LAUNCH_DONE = "has_launched_before"

    private val mainHandler = Handler(Looper.getMainLooper())

    private var isInitialized = false

    // App Open background tracking
    private var appBackgroundTimestampMs = 0L

    // Sacred session tracking: strictly protects prayer, audio recitation, and Jap Mala chanting
    @Volatile
    private var isAudioPlaying = false
    @Volatile
    private var isJapMalaActive = false

    // App Open Ad State
    private var appOpenAd: AppOpenAd? = null
    private var isAppOpenLoading = false
    private var appOpenLoadTimeMs = 0L
    private var lastAppOpenShowTimeMs = 0L
    private const val APP_OPEN_EXPIRY_MS = 4 * 3600 * 1000L // 4 hours
    private const val APP_OPEN_COOLDOWN_MS = 30_000L // 30 seconds

    // Interstitial Ad State
    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false
    private var lastInterstitialShowTimeMs = 0L
    private const val INTERSTITIAL_COOLDOWN_MS = 35_000L // 35 seconds

    // Rewarded Ad State (Optional Bonus Audio Content Unlock)
    private var rewardedAd: RewardedAd? = null
    @Volatile
    private var isRewardedLoading = false
    private val rewardedLoadListeners = mutableListOf<Pair<(() -> Unit)?, ((String) -> Unit)?>>()

    // Preloaded Native Ad State for quick list rendering
    private var cachedNativeAd: NativeAd? = null
    private var isNativeLoading = false

    fun setAudioPlaying(playing: Boolean) {
        isAudioPlaying = playing
    }

    fun setJapMalaActive(active: Boolean) {
        isJapMalaActive = active
    }

    /**
     * Centralized sacred session protection:
     * Suppresses full-screen ads during active chanting, audio playback, or Jap Mala sessions.
     */
    fun isSacredSessionActive(): Boolean = isAudioPlaying || isJapMalaActive

    /**
     * Initializes the Next-Gen Mobile Ads SDK with UMP Consent handling.
     */
    fun initialize(activity: Activity, onInitialized: () -> Unit = {}) {
        if (isInitialized) {
            onInitialized()
            return
        }

        try {
            val consentInfo = UserMessagingPlatform.getConsentInformation(activity)
            val params = ConsentRequestParameters.Builder().build()

            consentInfo.requestConsentInfoUpdate(
                activity,
                params,
                {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            Log.w(TAG, "UMP Consent form error: ${formError.message}")
                        }
                        if (consentInfo.canRequestAds()) {
                            initNextGenSdk(activity.applicationContext, onInitialized)
                        } else {
                            onInitialized()
                        }
                    }
                },
                { consentError ->
                    Log.w(TAG, "UMP Consent request failed: ${consentError.message}")
                    if (consentInfo.canRequestAds()) {
                        initNextGenSdk(activity.applicationContext, onInitialized)
                    } else {
                        onInitialized()
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in initialize", e)
            initNextGenSdk(activity.applicationContext, onInitialized)
        }
    }

    private fun initNextGenSdk(appContext: Context, onInitialized: () -> Unit) {
        if (isInitialized) {
            onInitialized()
            return
        }
        try {
            val reqConfig = RequestConfiguration.Builder().build()

            val initConfig = InitializationConfig.Builder(AdMobConfig.getAppId())
                .setRequestConfiguration(reqConfig)
                .build()

            MobileAds.initialize(appContext, initConfig) { status ->
                Log.d(TAG, "Google Next-Gen Mobile Ads initialized: $status")
                isInitialized = true
                preloadAds(appContext)
                mainHandler.post { onInitialized() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Next-Gen Mobile Ads initialization failed", e)
            mainHandler.post { onInitialized() }
        }
    }

    private fun preloadAds(context: Context) {
        loadAppOpenAd(context)
        loadInterstitialAd(context)
        loadRewardedAd(context)
        loadNativeAd(context)
    }

    // =========================================================================
    // 1. APP OPEN ADS (Compliant with Google Policy & Frequency Limits)
    // =========================================================================

    fun loadAppOpenAd(context: Context) {
        if (appOpenAd != null || isAppOpenLoading) return
        isAppOpenLoading = true
        val request = AdRequest.Builder(AdMobConfig.getAppOpenAdUnitId()).build()
        AppOpenAd.load(
            request,
            object : AdLoadCallback<AppOpenAd> {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "Next-Gen App Open Ad loaded successfully")
                    appOpenAd = ad
                    appOpenLoadTimeMs = System.currentTimeMillis()
                    isAppOpenLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Next-Gen App Open Ad failed to load: ${error.message}")
                    appOpenAd = null
                    isAppOpenLoading = false
                }
            }
        )
    }

    /**
     * Called when the app is brought to foreground (warm/hot start).
     * Enforces Google policies:
     * - Never shows on the very first launch.
     * - Never shows on cold start directly over main content (prevents late ad disruption).
     * - Only shows on warm starts where user was away in background for >= 3 seconds.
     * - Suppressed during sacred sessions (audio recitation or active Jap Mala).
     */
    fun onAppForegrounded(activity: Activity) {
        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val hasLaunchedBefore = prefs.getBoolean(KEY_FIRST_LAUNCH_DONE, false)
        if (!hasLaunchedBefore) {
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH_DONE, true).apply()
            Log.d(TAG, "First launch ever: suppressing App Open Ad per Google policy")
            return
        }

        // On cold start, do NOT show late ads after user reaches main content
        if (appBackgroundTimestampMs == 0L) {
            Log.d(TAG, "Cold start directly into main content: suppressing App Open Ad to avoid late disruption")
            return
        }

        // Must have been in background for at least 3 seconds
        val backgroundDuration = System.currentTimeMillis() - appBackgroundTimestampMs
        if (backgroundDuration < 3000L) {
            Log.d(TAG, "App returned too quickly ($backgroundDuration ms): suppressing App Open Ad")
            return
        }

        showAppOpenAdIfAvailable(activity) {}
    }

    fun onAppBackgrounded() {
        appBackgroundTimestampMs = System.currentTimeMillis()
    }

    fun showAppOpenAdIfAvailable(activity: Activity, onComplete: () -> Unit) {
        if (activity.isFinishing || activity.isDestroyed) {
            onComplete()
            return
        }
        if (isSacredSessionActive()) {
            Log.d(TAG, "Sacred devotional session active; suppressing App Open ad")
            onComplete()
            return
        }

        val ad = appOpenAd
        val currentTime = System.currentTimeMillis()
        val isExpired = (currentTime - appOpenLoadTimeMs) > APP_OPEN_EXPIRY_MS
        val isInCooldown = (currentTime - lastAppOpenShowTimeMs) < APP_OPEN_COOLDOWN_MS

        if (ad == null || isExpired || isInCooldown) {
            if (ad == null || isExpired) {
                appOpenAd = null
                loadAppOpenAd(activity.applicationContext)
            }
            onComplete()
            return
        }

        ad.adEventCallback = object : AppOpenAdEventCallback {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Next-Gen App Open Ad dismissed")
                appOpenAd = null
                lastAppOpenShowTimeMs = System.currentTimeMillis()
                loadAppOpenAd(activity.applicationContext)
                mainHandler.post { onComplete() }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w(TAG, "Next-Gen App Open Ad failed to show: ${error.message}")
                appOpenAd = null
                loadAppOpenAd(activity.applicationContext)
                mainHandler.post { onComplete() }
            }
        }

        ad.show(activity)
    }

    // =========================================================================
    // 2. INTERSTITIAL ADS (Controlled Natural Transitions)
    // =========================================================================

    fun loadInterstitialAd(context: Context) {
        if (interstitialAd != null || isInterstitialLoading) return
        isInterstitialLoading = true
        val request = AdRequest.Builder(AdMobConfig.getInterstitialAdUnitId()).build()
        InterstitialAd.load(
            request,
            object : AdLoadCallback<InterstitialAd> {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Next-Gen Interstitial Ad loaded successfully")
                    interstitialAd = ad
                    isInterstitialLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Next-Gen Interstitial Ad failed to load: ${error.message}")
                    interstitialAd = null
                    isInterstitialLoading = false
                }
            }
        )
    }

    /**
     * Shows an Interstitial Ad during natural transitions.
     * Respects 35s cooldown, never blocks navigation, and never interrupts sacred audio/Jap Mala.
     */
    fun showInterstitialAd(activity: Activity?, onComplete: () -> Unit) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            onComplete()
            return
        }
        if (isSacredSessionActive()) {
            onComplete()
            return
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInterstitialShowTimeMs < INTERSTITIAL_COOLDOWN_MS) {
            onComplete()
            return
        }

        val ad = interstitialAd
        if (ad == null) {
            loadInterstitialAd(activity.applicationContext)
            onComplete()
            return
        }

        ad.adEventCallback = object : InterstitialAdEventCallback {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Next-Gen Interstitial Ad dismissed")
                interstitialAd = null
                lastInterstitialShowTimeMs = System.currentTimeMillis()
                loadInterstitialAd(activity.applicationContext)
                mainHandler.post { onComplete() }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w(TAG, "Next-Gen Interstitial Ad failed to show: ${error.message}")
                interstitialAd = null
                loadInterstitialAd(activity.applicationContext)
                mainHandler.post { onComplete() }
            }
        }

        ad.show(activity)
    }

    // =========================================================================
    // 3. REWARDED ADS – BONUS AUDIO CONTENT UNLOCK (100% Optional)
    // =========================================================================

    fun isRewardedAdReady(): Boolean = rewardedAd != null && !isRewardedLoading

    fun isRewardedAdLoading(): Boolean = isRewardedLoading

    fun loadRewardedAd(
        context: Context,
        onLoaded: (() -> Unit)? = null,
        onFailed: ((String) -> Unit)? = null
    ) {
        if (rewardedAd != null) {
            mainHandler.post { onLoaded?.invoke() }
            return
        }
        if (onLoaded != null || onFailed != null) {
            synchronized(rewardedLoadListeners) {
                rewardedLoadListeners.add(onLoaded to onFailed)
            }
        }
        if (isRewardedLoading) {
            // Already in flight; the listener was added and will be notified upon completion
            Log.d(TAG, "Rewarded Ad is already loading; queuing listener")
            return
        }
        isRewardedLoading = true
        Log.d(TAG, "Requesting Next-Gen Rewarded Ad with ID: ${AdMobConfig.getRewardedAdUnitId()}")
        val request = AdRequest.Builder(AdMobConfig.getRewardedAdUnitId()).build()
        RewardedAd.load(
            request,
            object : AdLoadCallback<RewardedAd> {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Next-Gen Rewarded Ad loaded successfully")
                    rewardedAd = ad
                    isRewardedLoading = false
                    val listeners = synchronized(rewardedLoadListeners) {
                        val copy = ArrayList(rewardedLoadListeners)
                        rewardedLoadListeners.clear()
                        copy
                    }
                    mainHandler.post {
                        listeners.forEach { it.first?.invoke() }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Next-Gen Rewarded Ad failed to load: ${error.message} (code: ${error.code})")
                    rewardedAd = null
                    isRewardedLoading = false
                    val listeners = synchronized(rewardedLoadListeners) {
                        val copy = ArrayList(rewardedLoadListeners)
                        rewardedLoadListeners.clear()
                        copy
                    }
                    mainHandler.post {
                        listeners.forEach { it.second?.invoke(error.message) }
                    }
                }
            }
        )
    }

    /**
     * Optional Rewarded Ad to permanently unlock a designated Bonus/Premium Audio Track.
     * Complies strictly with Google AdMob & Play policy:
     * - Skipping or declining does NOT impede normal audio or Jap Mala usage.
     * - Standard audio and Jap Mala remain 100% free and unblocked.
     * - Permanently unlocks the track in AdMobService only after the official earned-reward callback.
     * - Never reuses an already consumed ad.
     * - Preloads the next Rewarded Ad immediately after dismissal.
     * - Prevents duplicate reward grants.
     */
    fun showBonusTrackRewardedAd(
        activity: Activity?,
        trackId: String,
        trackTitle: String,
        lang: String = "gu",
        onUnlocked: () -> Unit,
        onDismissedWithoutReward: (reason: String) -> Unit
    ) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            mainHandler.post { onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang)) }
            return
        }

        val ad = rewardedAd
        if (ad == null) {
            // Ad not ready yet; initiate preload and inform user with clear localized message
            loadRewardedAd(activity.applicationContext)
            mainHandler.post {
                onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang))
            }
            return
        }

        // CONSUME IMMEDIATELY to prevent double-showing or reuse of consumed ad
        rewardedAd = null

        var rewardConfirmed = false
        var isRewardGranted = false

        ad.adEventCallback = object : RewardedAdEventCallback {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Next-Gen Rewarded Ad dismissed. Confirmed: $rewardConfirmed")
                // Automatically reload next Rewarded Ad after previous is dismissed
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    if (rewardConfirmed && !isRewardGranted) {
                        isRewardGranted = true
                        AdMobService.unlockTrack(activity.applicationContext, trackId)
                        onUnlocked()
                    } else if (!rewardConfirmed) {
                        onDismissedWithoutReward(AppStrings.bonusAudioDismissedEarly(lang))
                    }
                }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w(TAG, "Next-Gen Rewarded Ad failed to show: ${error.message}")
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    onDismissedWithoutReward(AppStrings.bonusAudioAdFailed(error.message, lang))
                }
            }
        }

        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type} for track: $trackId")
            rewardConfirmed = true
        })
    }

    /**
     * Optional Rewarded Ad to earn +108 Divine Mala Beads / 1 Full Mala Round.
     * Complies strictly with Google AdMob & Play policy.
     */
    fun showMalaBonusRewardedAd(
        activity: Activity?,
        lang: String = "gu",
        onBonusGranted: () -> Unit,
        onDismissedWithoutReward: (reason: String) -> Unit
    ) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            mainHandler.post { onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang)) }
            return
        }

        val ad = rewardedAd
        if (ad == null) {
            loadRewardedAd(activity.applicationContext)
            mainHandler.post {
                onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang))
            }
            return
        }

        rewardedAd = null
        var rewardConfirmed = false
        var isRewardGranted = false

        ad.adEventCallback = object : RewardedAdEventCallback {
            override fun onAdDismissedFullScreenContent() {
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    if (rewardConfirmed && !isRewardGranted) {
                        isRewardGranted = true
                        onBonusGranted()
                    } else if (!rewardConfirmed) {
                        onDismissedWithoutReward(AppStrings.bonusAudioDismissedEarly(lang))
                    }
                }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w(TAG, "Mala Rewarded Ad failed to show: ${error.message}")
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    onDismissedWithoutReward(AppStrings.bonusAudioAdFailed(error.message, lang))
                }
            }
        }

        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            Log.d(TAG, "User earned Mala 108 beads reward: ${rewardItem.amount}")
            rewardConfirmed = true
        })
    }

    /**
     * Optional Rewarded Ad to unlock premium HD Wallpaper download/sharing.
     */
    fun showWallpaperRewardedAd(
        activity: Activity?,
        wallpaperName: String,
        lang: String = "gu",
        onUnlocked: () -> Unit,
        onDismissedWithoutReward: (reason: String) -> Unit
    ) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            mainHandler.post { onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang)) }
            return
        }

        val ad = rewardedAd
        if (ad == null) {
            loadRewardedAd(activity.applicationContext)
            mainHandler.post {
                onDismissedWithoutReward(AppStrings.bonusAudioAdUnavailable(lang))
            }
            return
        }

        rewardedAd = null
        var rewardConfirmed = false
        var isRewardGranted = false

        ad.adEventCallback = object : RewardedAdEventCallback {
            override fun onAdDismissedFullScreenContent() {
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    if (rewardConfirmed && !isRewardGranted) {
                        isRewardGranted = true
                        onUnlocked()
                    } else if (!rewardConfirmed) {
                        onDismissedWithoutReward(AppStrings.bonusAudioDismissedEarly(lang))
                    }
                }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w(TAG, "Wallpaper Rewarded Ad failed to show: ${error.message}")
                loadRewardedAd(activity.applicationContext)
                mainHandler.post {
                    onDismissedWithoutReward(AppStrings.bonusAudioAdFailed(error.message, lang))
                }
            }
        }

        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            Log.d(TAG, "User earned Wallpaper unlock: $wallpaperName")
            rewardConfirmed = true
        })
    }

    // =========================================================================
    // 4. NATIVE ADS
    // =========================================================================

    fun loadNativeAd(context: Context, onLoaded: ((NativeAd) -> Unit)? = null) {
        if (cachedNativeAd != null) {
            onLoaded?.invoke(cachedNativeAd!!)
            return
        }
        if (isNativeLoading) return
        isNativeLoading = true

        val request = NativeAdRequest.Builder(
            AdMobConfig.getNativeAdUnitId(),
            listOf(NativeAd.NativeAdType.NATIVE)
        ).build()

        NativeAdLoader.load(
            request,
            object : NativeAdLoaderCallback {
                override fun onNativeAdLoaded(ad: NativeAd) {
                    Log.d(TAG, "Next-Gen Native Ad loaded successfully")
                    cachedNativeAd = ad
                    isNativeLoading = false
                    mainHandler.post { onLoaded?.invoke(ad) }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Next-Gen Native Ad failed to load: ${error.message}")
                    isNativeLoading = false
                }
            }
        )
    }

    fun getCachedNativeAd(): NativeAd? = cachedNativeAd

    /**
     * Backward-compatible helper for transitions between sub-screens.
     * Uses Interstitial Ads exclusively (never Rewarded Ads for general navigation).
     */
    fun showAdThenProceed(
        activity: Activity?,
        forceShow: Boolean = false,
        onProceed: () -> Unit
    ) {
        showInterstitialAd(activity, onProceed)
    }
}
